package com.ta.sia.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.ForeignKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.ta.sia.annotation.Orderable;
import com.ta.sia.config.ValidGroup.Update;
import com.ta.sia.config.View;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.ManyToOne;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
//
//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.Size;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter @Getter
public class Persediaan extends AbstractEntity {
	
	@JsonView(View.All.class)
	@Orderable
	private String barang;
	
	@JsonView(View.All.class)
	@JsonIgnoreProperties(value = {"parent","jurnalViewDetails"})
	@ManyToOne(fetch = FetchType.EAGER)
	private Rekening rekHpp;
	
	@JsonView(View.All.class)
	@JsonIgnoreProperties(value = {"parent","jurnalViewDetails"})
	@ManyToOne(fetch = FetchType.EAGER)
	private Rekening rekPenjualan;
	
	@JsonView(View.All.class)
	@JsonIgnoreProperties(value = {"parent","jurnalViewDetails"})
	@ManyToOne(fetch = FetchType.EAGER)
	private Rekening rekInventory;
	
	@JsonView(View.All.class)
	@Transient
	private static Date periode = new Date(System.currentTimeMillis());
	
	@JsonView(View.All.class)
	private Double qtyAwal = Double.valueOf(0);
	
	@JsonView(View.All.class)
	private Double hargaAwal = Double.valueOf(0);
	
	@JsonView(View.All.class)
	private String satuan;

	@OneToMany(mappedBy = "barang", cascade = CascadeType.REMOVE)
	@JsonIgnore
	private List<TransaksiDetails1> transaksiDetails1s = new ArrayList<TransaksiDetails1>();
	
	@JsonIgnoreProperties(value = {"persediaan"})
	@OneToMany(mappedBy = "persediaan")
	private List<Stok> stok = new ArrayList<Stok>();
	
	@JsonView(View.All.class)
	private Timestamp createdAt = new Timestamp(System.currentTimeMillis());
	
	@Transient
	@JsonIgnore
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Transient
	private String metode = "FIFO";

	public Persediaan() {
		Stok stok = new Stok();
		stok.setKeterangan("Stok awal");
		stok.setPersediaan(this);
		this.stok.add(stok);
	}

	public Persediaan(Long id, String barang, Rekening rekHpp, Rekening rekPenjualan, Rekening rekInventory,
			Timestamp createdAt) {
		this.id = id;
		this.barang = barang;
		this.rekHpp = rekHpp;
		this.rekPenjualan = rekPenjualan;
		this.rekInventory = rekInventory;
		this.createdAt = createdAt;
	}

	public Persediaan(Persediaan persediaan) {
		this.id = persediaan.id;
		this.barang = persediaan.barang;
		List<Stok> ls = new ArrayList<Stok>();
		for (Stok stok : persediaan.stok) {
			ls.add(new Stok(stok));
		}
		this.stok = ls;
		for (TransaksiDetails1 pd : persediaan.getTransaksiDetails1s()) {
			this.transaksiDetails1s
					.add(new PembelianDetails(pd.getId(), pd.getHarga(), pd.getJumlah(), pd.getCreatedAt()));
		}
		for (TransaksiDetails1 pd : persediaan.getTransaksiDetails1s()) {
			this.transaksiDetails1s
					.add(new PenjualanDetails(pd.getId(), pd.getHarga(), pd.getJumlah(), pd.getCreatedAt()));
		}
	}

	public void addStok(Stok stok) {
		stok.setPersediaan(this);
		this.stok.add(stok);
	}

	public List<TransaksiDetails1> getTransaksiDetails1s() {
		List<TransaksiDetails1> result = new ArrayList<TransaksiDetails1>();
		for (TransaksiDetails1 pd : this.transaksiDetails1s) {
			if (pd.getTransaksi().getTanggal().compareTo(periode) <= 0) {
				result.add(pd);
			}
		}
		return result;
	}

	@Transient
	public Double getStokAkhir() {
//		this.generateStok();
//		if (this.stok.size() <= 0) {
//			return Double.valueOf(0);
//		}
//		Stok stok = this.stok.get(this.stok.size() - 1);
		Double stokAkhir = Double.valueOf("0");
//		for (StokDetails sd : stok.getStokDetails()) {
//			stokAkhir += sd.getQty() * sd.getHarga();
//		}
		return stokAkhir;
		
	}

	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return this.getBarang();
	}

}