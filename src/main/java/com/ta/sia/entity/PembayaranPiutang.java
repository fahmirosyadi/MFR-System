package com.ta.sia.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.ta.sia.annotation.Orderable;
import com.ta.sia.config.View.All;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
@Entity
public class PembayaranPiutang extends Transaksi{

	@NotBlank(message = "Bukti Kas Masuk harus diisi")
	@Orderable
	@JsonView(All.class)
	private String buktiKasMasuk;
	
	@OneToMany(mappedBy = "pembayaranPiutang", cascade = CascadeType.ALL)
	@JsonIgnoreProperties({"pembayaranPiutang"})
	@Valid
	private List<PembayaranPiutangDetails> pembayaranPiutangDetails;
	
	public void setPembayaranPiutangDetails(List<PembayaranPiutangDetails> pembayaranPiutangDetails){
		for(PembayaranPiutangDetails pd : pembayaranPiutangDetails) {
			pd.setPembayaranPiutang(this);
		}
		this.pembayaranPiutangDetails = pembayaranPiutangDetails;
	}
	
	public PembayaranPiutang() {
		this.jenis = "pembayaran-piutang";
	}
	
	public Customer getCustomer() {
		if(this.getPembayaranPiutangDetails() != null && this.getPembayaranPiutangDetails().size() > 0) {
			return this.getPembayaranPiutangDetails().get(0).getPenjualan().getCustomer();
		}
		return null;
	}

}
