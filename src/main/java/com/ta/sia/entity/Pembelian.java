package com.ta.sia.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ColumnDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ta.sia.annotation.Orderable;
import com.ta.sia.config.View;
import com.ta.sia.config.View.All;
import com.ta.sia.validation.MustHasAnId;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.ManyToOne;
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import java.util.ArrayList;

@Entity
@Setter @Getter
public class Pembelian extends Transaksi {

	@NotBlank(message = "No Faktur harus diisi")
	@Orderable
	@JsonView(All.class)
	private String faktur;
	@ManyToOne
	@JsonIgnoreProperties({"pembelian","bukuPembantuUtang"})
	@NotNull(message = "Supplier harus diisi")
	@MustHasAnId(message = "Supplier harus diisi")
	private Supplier supplier;
	private Double bayar;
	@JsonIgnore
	@OneToMany(mappedBy = "pembelian", cascade = CascadeType.ALL)
	private List<PembayaranUtangDetails> pembayaranUtangDetails;
	
	@Valid
	@Size(min = 1,message = "Pembelian Details tidak boleh kosong")
	@JsonIgnoreProperties(value = {"pembelian"}, allowSetters = true)
	@OneToMany(mappedBy = "pembelian", cascade = CascadeType.ALL)
	private List<PembelianDetails> pembelianDetails = new ArrayList<>();
	
	@JsonIgnore
	@Transient
	Logger logger = LoggerFactory.getLogger(this.getClass());

	public Pembelian() {
		this.jenis = "pembelian";
	}

	public void setPembelianDetails(List<PembelianDetails> pembelianDetails){
		for(PembelianDetails pd : pembelianDetails) {
			pd.setPembelian(this);
			pd.setTransaksi(this);
		}
		this.pembelianDetails = pembelianDetails;
	}
	
	public Double getTotalUtang() {
		Double bayarUtang = Double.valueOf(0);
		if(this.pembayaranUtangDetails != null) {
			for(PembayaranUtangDetails pu : this.pembayaranUtangDetails) {
				bayarUtang += pu.getBayar();
			}
		}
		
		return this.getTotalBersih() - ((this.getBayar() != null ? this.getBayar() : 0) + bayarUtang);
	}
	
	public void setJenis(String jenis) {
		this.jenis = "pembelian";
	}
	
	public void setBayar(Long bayar) {
		this.bayar = bayar != null ? bayar : Double.valueOf(0);
	}
	
}
