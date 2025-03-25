package com.ta.sia.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Formula;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ta.sia.validation.MustHasAnId;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Setter @Getter @NoArgsConstructor @ToString
public abstract class TransaksiDetails extends TransaksiDetails1{
	
	@NotNull(message = "Harga harus diisi")
	protected Double harga;
	
	protected Double diskon = Double.valueOf(0);
	
	@ManyToOne
	protected Pajak pajak;

	@Formula(value = "get_td_total(id)")
	protected Double total;
	
	@Formula(value = "get_td_total_after_dis(id)")
	protected Double totalSetelahDiskon;
	
	@Formula(value = "get_td_pajak(id)")
	protected Double totalPajak;
	
	@Formula(value = "get_td_total_bersih(id)")
	protected Double totalBersih;
	
	public void setPajak(Pajak pajak) {
		if(pajak != null && pajak.getId() != null) this.pajak = pajak;
	}
	
}
