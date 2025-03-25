package com.ta.sia.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Formula;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.Size;

import java.sql.Timestamp;

@Entity
@Setter @Getter @NoArgsConstructor @ToString
public class PembelianDetails extends TransaksiDetails{

	@JsonIgnoreProperties("pembelianDetails")
	@ManyToOne
	private Pembelian pembelian;	

	public PembelianDetails(Long id, Persediaan barang, Double harga, Double jumlah, Timestamp createdAt) {
		this.id = id;
		this.barang = barang;
		this.harga = harga;
		this.jumlah = jumlah;
		this.createdAt = createdAt;
	}
	
	public PembelianDetails(Long id, Double harga, Double jumlah, Timestamp createdAt) {
		this.id = id;
		this.harga = harga;
		this.jumlah = jumlah;
		this.createdAt = createdAt;
	}

//	@Override
//	protected Transaksi getTransaksi() {
//		return this.getPembelian();
//	}
	
}