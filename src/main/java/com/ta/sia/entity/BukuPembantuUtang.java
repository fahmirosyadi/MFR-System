package com.ta.sia.entity;

import java.sql.Date;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class BukuPembantuUtang {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Date tanggal;
	private String faktur;
	private String keterangan;
	
	@JsonIgnore
	@ManyToOne
	private Supplier supplier;
	private Double debet;
	private Double kredit;
	private Double saldo;
	
	
	
	public BukuPembantuUtang(Long id, Date tanggal, String faktur, String keterangan, Supplier supplier, Double debet,
			double kredit, Double saldo) {
		this.id = id;
		this.tanggal = tanggal;
		this.faktur = faktur;
		this.keterangan = keterangan;
		this.supplier = supplier;
		this.debet = debet;
		this.kredit = kredit;
		this.saldo = saldo;
	}
		
	
}
