package com.ta.sia.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.CascadeType;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.ta.sia.config.View.All;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.ArrayList;

@Entity
@Setter @Getter @ToString
public class Jurnal extends Transaksi{

//	@OneToOne
//	@JsonIgnore
//	private Transaksi transaksi;
	
	@Transient
	@JsonView(All.class)
	private String faktur;
	
	@Transient
	@JsonIgnore
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Valid
	@OrderBy(value = "id")
	@OneToMany(mappedBy = "jurnal", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//	@Size(min = 1,message = "Jurnal Details tidak boleh kosong")
	@JsonIgnoreProperties(value = {"jurnal"},allowSetters = true)
	@JsonView(All.class)
	private List<JurnalDetails> jurnalDetails = new ArrayList<>();
	@Transient
	private String detailsLink;

	public Jurnal() {
		this.setJenis("jurnal");
	}
	
	public void setJurnalDetails(List<JurnalDetails> jurnalDetails){
		for(JurnalDetails jd : jurnalDetails) {
			jd.setJurnal(this);
			logger.info("Add jurnal details : " + jd.getRekening().getNama());
		}
		this.jurnalDetails = jurnalDetails;
		
	}
	
	public void addDetail(JurnalDetails jurnalDetails){
		jurnalDetails.setJurnal(this);
		jurnalDetails.setId(null);
		this.jurnalDetails.add(jurnalDetails);
	}
	
	
	@Transient
	@AssertTrue(message = "Jurnal harus balance")
	public boolean isBalance() {
		if(!this.getJenis().equals("jurnal")) {
			return true;
		}
		Double total = Double.valueOf(0);
		for(JurnalDetails jd : this.jurnalDetails) {
			if(jd.isEmpty() == false)total += jd.getDebet() - jd.getKredit();
		}
		if(total.equals(Double.valueOf(0)))return true;
		return false;
	}
	
	@Transient
	public Double getTotal() {
		Double total = Double.valueOf(0);
		for(JurnalDetails jd : this.jurnalDetails) {
			total += jd.getDebet();
		}
		
		return total;
	}

	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return this.getKeterangan();
	}

	@Override
	public Long getValue() {
		// TODO Auto-generated method stub
		return this.getId();
	}
	
	@Override
	@JsonIgnore
	public JurnalView getJurnalView() {
		// TODO Auto-generated method stub
		return super.getJurnalView();
	}

}