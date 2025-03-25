package com.ta.sia.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.ForeignKey;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Entity
@Setter @Getter @NoArgsConstructor
public class StokDetails {
	
	@Id
	private String id;
	private Double qty;
	private Double harga;
	@ManyToOne
	@ForeignKey(name = "none")
	@JoinColumn(name = "stok_id")
	@JsonIgnoreProperties({"stokDetails"})
	private Stok stok;
	
	public StokDetails(Double qty, Double harga) {
		this.qty = qty;
		this.harga = harga;
	}
}
