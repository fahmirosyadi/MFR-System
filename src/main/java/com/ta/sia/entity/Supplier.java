package com.ta.sia.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.engine.FetchStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.ta.sia.annotation.Orderable;
import com.ta.sia.config.View.All;
import com.ta.sia.config.View.Detail;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter @Getter @NoArgsConstructor
public class Supplier extends AbstractEntity{

	@NotBlank(message = "Nama Supplier tidak boleh kosong")
	@Orderable
	@JsonView(All.class)
	private String nama;
	@Orderable
	@JsonView(All.class)
	private String alamat;
	@Orderable()
	@JsonView(All.class)
	private String telp;
	@Transient
	private List<BukuPembantuUtang> bukuPembantuUtang = new ArrayList<BukuPembantuUtang>();
	private Long saldoAwal = Long.valueOf("0");
	@Transient
	@JsonIgnore
	private Double totalUtang;
	@JsonIgnoreProperties({"supplier","jurnalView","pembelianDetails"})
	@OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Pembelian> pembelian = new ArrayList<Pembelian>();

	
	@JsonIgnore
	@Transient
	public Double getSaldo() {
		Double result = Double.valueOf(0);
		result += this.getSaldoAwal();
		for(Pembelian p : this.pembelian) {
			result += p.getTotalBersih() - (p.getBayar() != null ? p.getBayar() : Double.valueOf(0));
		}
		return result;
	}
	
	
	@Transient
	@JsonView(Detail.class)
	public List<BukuPembantuUtang> getBukuPembantuUtang() {
		
		this.bukuPembantuUtang = new ArrayList<BukuPembantuUtang>();
		System.out.println("Add buku pembantu utang");
		for(Pembelian p : this.pembelian) {
			this.bukuPembantuUtang.add(new BukuPembantuUtang(p.getId(), p.getTanggal(), p.getFaktur(), p.getKeterangan(), p.getSupplier(), Double.valueOf(0), p.getTotal() - (p.getBayar() != null? p.getBayar() : 0), Double.valueOf(0)));
			for(PembayaranUtangDetails pud : p.getPembayaranUtangDetails()) {
				this.bukuPembantuUtang.add(new BukuPembantuUtang(pud.getId(), pud.getPembayaranUtang().getTanggal(), pud.getPembayaranUtang().getBuktiKasKeluar(), pud.getPembayaranUtang().getKeterangan(), p.getSupplier(), pud.getBayar(), 0, Double.valueOf(0)));
			}
		}
		System.out.println("Success add buku pembantu utang");
		Double saldo = Double.valueOf(0);
		for(BukuPembantuUtang utang : this.bukuPembantuUtang) {
			saldo += utang.getKredit() - utang.getDebet();
			utang.setSaldo(saldo);
		}
		this.totalUtang = saldo;
		return this.bukuPembantuUtang;
	}

	@Override
	public String getLabel() {
		return this.nama;
	}

	@Override
	public Long getValue() {
		return this.id;
	}	

}