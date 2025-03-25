package com.ta.sia.entity;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Formula;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.ta.sia.annotation.Orderable;
import com.ta.sia.config.View.All;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Setter @Getter @ToString @NoArgsConstructor
@JsonIgnoreProperties(value = {"jurnal"}, allowGetters = true)
public abstract class Transaksi extends AbstractEntity {
	
	@NotNull(message = "Tanggal harus diisi")
	@JsonView(All.class)
	protected Date tanggal;
	@NotBlank(message = "Keterangan harus diisi")
	@JsonView(All.class)
	protected String keterangan;
	@JsonView(All.class)
	protected String jenis;
	protected Timestamp createdAt = new Timestamp(System.currentTimeMillis());
	@OneToOne(mappedBy = "transaksi")
	@JsonView(All.class)
	protected JurnalView jurnalView;
	@Formula("get_t_total(id)")
	protected Double total;
	@Formula("get_t_diskon(id)")
	protected Double diskon;
	@Formula("get_t_pajak(id)")
	protected Double totalPajak;
	
	public Double getTotalSetelahDiskon() {
		System.out.println("Total : " + this.getTotal());
		if(this.getTotal() != null) {
			return this.getTotal() - (this.getDiskon() != null ? this.getDiskon() : Double.valueOf(0));
		}
		return null;
	}
	
	public Double getTotalBersih() {
		if(this.getTotalSetelahDiskon() != null) {
			return this.getTotalSetelahDiskon() + (this.getTotalPajak() != null ? this.getTotalPajak() : Double.valueOf(0));
		}
		return null;
	}
	
	@Override
	public Long getValue() {
		return this.getId();
	}
	
	@Override
	public String getLabel() {
		return this.getKeterangan();
	}
	
}
