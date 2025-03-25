package com.ta.sia.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ta.sia.validation.MustHasAnId;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Setter @Getter @NoArgsConstructor @ToString
public abstract class TransaksiDetails1 {
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	protected Long id;
	@NotNull(message = "Barang harus diisi")
	@JsonIgnoreProperties({"rekHpp","rekPenjualan","rekInventory","transaksiDetails1s","stok"})
	@ManyToOne
	@MustHasAnId(message = "Persediaan tidak boleh kosong")
	protected Persediaan barang;
	protected Double jumlah;
	protected Timestamp createdAt = new Timestamp(System.currentTimeMillis());
	@ManyToOne
	@JsonIgnore
	protected Transaksi transaksi;
	protected Double getHarga() {
		return null;
	}
}
