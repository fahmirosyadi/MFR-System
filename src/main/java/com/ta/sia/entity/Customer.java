package com.ta.sia.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.Size;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.ta.sia.annotation.Orderable;
import com.ta.sia.config.View;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
@Getter @Setter @NoArgsConstructor
public class Customer extends AbstractEntity {
	
	@JsonView(View.All.class)
	@NotBlank(message = "Nama Customer tidak boleh kosong")
	@Orderable
	private String nama;
	
	@JsonView(View.All.class)
	@Orderable
	private String alamat;
	
	@JsonView(View.All.class)
	@Orderable
	private String telp;
	
	@JsonView(View.All.class)
	private Long saldoAwal = Long.valueOf("0");
	
	@Transient
	private List<BukuPembantuPiutang> bukuPembantuPiutang = new ArrayList<BukuPembantuPiutang>();
	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
	private List<Penjualan> penjualan = new ArrayList<Penjualan>();

	@Transient
	public Double getSaldo() {
		Double result = Double.valueOf(0);
		result += this.getSaldoAwal();
		for(Penjualan p : this.penjualan) {
			result += p.getTotalBersih() - (p.getBayar() != null ? p.getBayar() : Double.valueOf(0));
		}
		return result;
	}
	
	@Transient
	public List<BukuPembantuPiutang> getBukuPembantuPiutang() {
		this.bukuPembantuPiutang = new ArrayList<BukuPembantuPiutang>();
		for(Penjualan p : this.penjualan) {
			this.bukuPembantuPiutang.add(new BukuPembantuPiutang(p.getId(), p.getTanggal(), p.getFaktur(), p.getKeterangan(), p.getCustomer(), p.getTotal() - (p.getBayar() != null ? p.getBayar() : 0), Double.valueOf(0)));
			for(PembayaranPiutangDetails ppd : p.getPembayaranPiutangDetails()) {
				this.bukuPembantuPiutang.add(new BukuPembantuPiutang(ppd.getId(), ppd.getPembayaranPiutang().getTanggal(), ppd.getPembayaranPiutang().getBuktiKasMasuk(), ppd.getPembayaranPiutang().getKeterangan(), p.getCustomer(), Double.valueOf(0), ppd.getBayar()));	
			}
		}
		Double saldo = Double.valueOf(0);
		for(BukuPembantuPiutang piutang : this.bukuPembantuPiutang) {
			saldo += piutang.getDebet() - piutang.getKredit();
			piutang.setSaldo(saldo);
		}
		return bukuPembantuPiutang;
	}

	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return this.nama;
	}

	@Override
	public Long getValue() {
		// TODO Auto-generated method stub
		return this.id;
	}	
	
}