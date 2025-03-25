package com.ta.sia.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.ta.sia.config.View.All;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.Size;

import java.sql.Timestamp;

@Entity
@Setter @Getter @NoArgsConstructor
public class JurnalDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@JsonIgnoreProperties(value = {"jurnalDetails"},allowSetters = true)
	@ManyToOne
	private Jurnal jurnal;
	@NotNull(message = "Rekening tidak boleh kosong")
	@ManyToOne
	@JsonView(All.class)
	@JsonIgnoreProperties(value = {"parent","jurnalDetails","jurnalViewDetails"},allowSetters = true)
	private Rekening rekening;
	@JsonView(All.class)
	private Double debet;
	@JsonView(All.class)
	private Double kredit;
	private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

	public JurnalDetails(JurnalDetails jd){
		this.id = jd.id;
		this.debet = jd.debet;
		this.kredit = jd.kredit;
	}
	
	public JurnalDetails(Rekening rekening, Double debet, Double kredit, Timestamp createdAt){
		this.rekening = rekening;
		this.debet = debet;
		this.kredit = kredit;
		this.createdAt = createdAt;
	}
	
	public JurnalDetails(Rekening rekening, Double debet, Double kredit, Timestamp createdAt, Jurnal jurnal){
		this.rekening = rekening;
		this.debet = debet;
		this.kredit = kredit;
		this.createdAt = createdAt;
		this.jurnal = jurnal;
	}


	@JsonIgnore
	public Timestamp getCreatedAt() {
		return createdAt;
	}

	
	@Transient
//	@AssertFalse(message = "Salah satu dari debet atau kredit harus lebih dari 0")
	public boolean isEmpty() {		
		if(this.debet == null)this.debet = Double.valueOf(0);
		if(this.kredit == null)this.kredit = Double.valueOf(0);
		if(this.debet + this.kredit <= 0) {
			return true;
		}
		return false;
	}

}