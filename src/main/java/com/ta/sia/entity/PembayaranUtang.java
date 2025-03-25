package com.ta.sia.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.ta.sia.annotation.Orderable;
import com.ta.sia.config.View.All;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
@Entity
public class PembayaranUtang extends Transaksi{

	@NotBlank(message = "Bukti Kas Keluar harus diisi")
	@Orderable
	@JsonView(All.class)
	private String buktiKasKeluar;
	
	@OneToMany(mappedBy = "pembayaranUtang", cascade = CascadeType.ALL)
	@JsonIgnoreProperties({"pembayaranUtang"})
	private List<PembayaranUtangDetails> pembayaranUtangDetails;
	
	public void setPembayaranUtangDetails(List<PembayaranUtangDetails> pembayaranUtangDetails){
		for(PembayaranUtangDetails pd : pembayaranUtangDetails) {
			pd.setPembayaranUtang(this);
		}
		this.pembayaranUtangDetails = pembayaranUtangDetails;
	}
	
	public PembayaranUtang() {
		this.jenis = "pembayaran-utang";
	}
	
	public Supplier getSupplier() {
		if(this.getPembayaranUtangDetails() != null && this.getPembayaranUtangDetails().size() > 0) {
			return this.getPembayaranUtangDetails().get(0).getPembelian().getSupplier();
		}
		return null;
	}

}
