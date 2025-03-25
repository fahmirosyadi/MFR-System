package com.ta.sia.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @ToString
@Entity
public class PembayaranPiutangDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JsonIgnoreProperties({"pembayaranPiutangDetails"})
	private PembayaranPiutang pembayaranPiutang;
	@ManyToOne
	@JsonIgnoreProperties({"customer"})
	private Penjualan penjualan;
	private Double diskon;
	private Double bayar;
	private Timestamp createdAt = new Timestamp(System.currentTimeMillis());
	
	public Double getTotalPiutang() {
		return this.penjualan.getTotalPiutang() + (this.bayar != null ? this.bayar : 0);
	}
	
}
