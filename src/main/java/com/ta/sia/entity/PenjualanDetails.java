package com.ta.sia.entity;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter @Getter @NoArgsConstructor
public class PenjualanDetails  extends TransaksiDetails{

	@JsonIgnoreProperties("penjualanDetails")
	@ManyToOne
	private Penjualan penjualan;
	
	public PenjualanDetails(Long id, Double harga, Double jumlah, Timestamp createdAt) {
		this.id = id;
		this.harga = harga;
		this.jumlah = jumlah;
		this.createdAt = createdAt;
	}
	
	
	@Transient
	public Double getHPP() {
//		List<Stok> listStok = this.barang.getStok();
//		Stok stok = new Stok();
//		Stok stokSebelum = new Stok();
//		for (Stok stok1 : listStok) {
//			if (stok1.getCreatedAt() == this.getCreatedAt()) {
//				stok = stokSebelum;
//			}
//			stokSebelum = stok1;
//		}
//		
		Double hpp = Double.valueOf("0");
//		Double qtyJual = this.getJumlah();
//		System.out.println(stok);
//		for (int i = 0; i < stok.getStokDetails().size(); i++) {
//			StokDetails fd = new StokDetails();
//			if (this.barang.getMetode().equals("FIFO")) {
//				fd = stok.getStokDetails().get(i);
//			} else if (this.barang.getMetode().equals("LIFO")) {
//				fd = stok.getStokDetails().get(stok.getStokDetails().size() - i - 1);
//			}
//			if (qtyJual <= fd.getQty()) {
//				hpp += qtyJual * fd.getHarga();
//				break;
//			} else {
//				hpp += fd.getQty() * fd.getHarga();
//				qtyJual -= fd.getQty();	
//			}
//		}
		return hpp;
	}


//	@Override
//	protected Transaksi getTransaksi() {
//		return this.getPenjualan();
//	}
}
