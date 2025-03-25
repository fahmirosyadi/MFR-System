package com.ta.sia.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class PenyesuaianPersediaanDetails extends TransaksiDetails1 {
	
	@JsonIgnoreProperties(value = {"parent","jurnalViewDetails"})
	@ManyToOne
	private Rekening rekening;
	@JsonIgnoreProperties(value = {"penyesuaianPersediaanDetails"})
	@ManyToOne
	private PenyesuaianPersediaan penyesuaianPersediaan;
//	@Override
//	protected Transaksi getTransaksi() {
//		return this.getBarangKeluar();
//	}
	
}
