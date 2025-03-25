package com.ta.sia.entity;

import java.sql.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class BukuPembantuPiutang {
	private Long id;
	private Date tanggal;
	private String faktur;
	private String keterangan;

	@JsonIgnore
	Customer customer;
	private Double debet;
	private Double kredit;
	private Double saldo;
	
	
	
	public BukuPembantuPiutang(Long id, Date tanggal, String faktur, String keterangan, Customer customer, double debet,
			Double kredit) {
		this.id = id;
		this.tanggal = tanggal;
		this.faktur = faktur;
		this.keterangan = keterangan;
		this.customer = customer;
		this.debet = debet;
		this.kredit = kredit;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	
	@ManyToOne
	public Customer getCustomer() {
		return customer;
	}	
	
}
