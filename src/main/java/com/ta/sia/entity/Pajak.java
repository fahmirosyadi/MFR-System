package com.ta.sia.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.ta.sia.annotation.Orderable;
import com.ta.sia.config.View.All;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter @NoArgsConstructor
@Entity
public class Pajak extends AbstractEntity {

	@Orderable
	@JsonView(All.class)
	private String nama;
	@JsonView(All.class)
	private double persen = 0;
	@ManyToOne
	@JsonIgnoreProperties({"child","parent","jurnalViewDetails"})
	@JsonView(All.class)
	private Rekening rekMasukkan;
	@ManyToOne
	@JsonIgnoreProperties({"child","parent","jurnalViewDetails"})
	@JsonView(All.class)
	private Rekening rekKeluaran;
	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return this.getNama() + " (" + this.getPersen() + "%)";
	}
	
}
