package com.ta.sia.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.ta.sia.annotation.Orderable;
import com.ta.sia.config.View.All;
import com.ta.sia.validation.MustHasAnId;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Setter @Getter @ToString
public class Penjualan extends Transaksi {

	@NotBlank(message = "No Faktur harus diisi")
	@Orderable
	@JsonView(All.class)
	private String faktur;
	@NotNull(message = "Customer harus diisi")
	@JsonIgnoreProperties({"penjualan","bukuPembantuPiutang"})
	@ManyToOne
	@MustHasAnId(message = "Customer harus diisi")
	private Customer customer;
	private Double bayar;
	@JsonIgnore
	@OneToMany(mappedBy = "penjualan", cascade = CascadeType.ALL)
	private List<PembayaranPiutangDetails> pembayaranPiutangDetails;
	
	public Penjualan() {
		this.jenis = "penjualan";
	}
	
	@Valid
	@JsonIgnoreProperties(value = {"penjualan"}, allowSetters = true)
	@Size(min = 1, message = "Penjualan Details tidak boleh kosong")
	@OneToMany(mappedBy = "penjualan", cascade = CascadeType.ALL)
	private List<PenjualanDetails> penjualanDetails = new ArrayList<PenjualanDetails>();

	@Transient
	@JsonIgnore
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public void setJenis(String jenis) {
		this.jenis = "penjualan";
	}
	
	public void setBayar(Double bayar) {
		this.bayar = bayar != null ? bayar : Double.valueOf(0);
	}

	public void setPenjualanDetails(List<PenjualanDetails> penjualanDetails) {
		for(PenjualanDetails pd : penjualanDetails) {
			pd.setPenjualan(this);
			pd.setTransaksi(this);
		}
		this.penjualanDetails = penjualanDetails;
	}
	
	public Double getTotalPiutang() {
		Double bayarPiutang = Double.valueOf(0);
		if(this.pembayaranPiutangDetails != null) {
			for(PembayaranPiutangDetails pu : this.pembayaranPiutangDetails) {
				bayarPiutang += pu.getBayar();
			}
		}
		return this.getTotalBersih() - ((this.getBayar() != null ? this.getBayar() : 0) + bayarPiutang);
	}
	
}
