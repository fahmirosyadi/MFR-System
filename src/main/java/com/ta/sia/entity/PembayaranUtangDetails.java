package com.ta.sia.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @ToString
@Entity
public class PembayaranUtangDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JsonIgnoreProperties({"pembayaranUtangDetails"})
	private PembayaranUtang pembayaranUtang;
	@ManyToOne
	@JsonIgnoreProperties({"supplier"})
	private Pembelian pembelian;
	private Double diskon;
	private Double bayar;
	private Timestamp createdAt = new Timestamp(System.currentTimeMillis());
	
	public Double getTotalUtang() {
		return this.pembelian.getTotalUtang() + (this.bayar != null ? this.bayar : 0);
	}
	
}
