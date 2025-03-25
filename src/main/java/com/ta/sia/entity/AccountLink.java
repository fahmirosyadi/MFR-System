package com.ta.sia.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

//import javax.validation.constraints.NotBlank;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class AccountLink {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@OneToOne
	private Rekening rekPembelian;
	@OneToOne
	private Rekening rekPotonganPembelian;
	@OneToOne
	private Rekening rekPenjualan;
	@OneToOne
	private Rekening rekPotonganPenjualan;
	@OneToOne
	private Rekening rekPembayaranUtang;
	@OneToOne
	private Rekening rekPembayaranPiutang;
	@OneToOne
	private Rekening rekLabaDitahan;
	@OneToOne
	private Rekening rekLabaTahunBerjalan;

	
	@Transient
	public boolean isLinked(Rekening rekening) {
		if(
			this.getRekLabaDitahan() == rekening || 
			this.getRekLabaTahunBerjalan() == rekening || 
			this.getRekPembayaranPiutang() == rekening || 
			this.getRekPembayaranUtang() == rekening || 
			this.getRekPenjualan() == rekening || 
			this.getRekPotonganPenjualan() == rekening || 
			this.getRekPembelian() == rekening || 
			this.getRekPotonganPembelian() == rekening) {
			return true;
		}
		return false;
	}
	

}