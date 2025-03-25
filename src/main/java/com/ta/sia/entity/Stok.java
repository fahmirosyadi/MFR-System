package com.ta.sia.entity;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.Size;

@Data
@Entity
@Immutable
@NoArgsConstructor
@Subselect("select * from stok")
public class Stok {

	@Id
	private String id;
	private Date tanggal;
	private String faktur;
	private String keterangan;
	private Double qtyBeli;
	private Double hargaBeli;
	private Double totalBeli;
	private Double qtyJual;
	private Double hargaJual;
	private Double totalJual;
	private Double qty;
	private Double harga;
	private Double total;
	
//	@OneToMany(mappedBy = "stok")
//	@ForeignKey(name = "none")
//	@JsonIgnoreProperties({"stok"})
//	private List<StokDetails> stokDetails = new ArrayList<StokDetails>();
//	
	@JsonIgnoreProperties({"rekHpp","rekPenjualan","rekInventory","pembelianDetails","penjualanDetails","stok","barangKeluarDetails"})
	@ManyToOne
	@JoinColumn(name = "persediaan_id")
	private Persediaan persediaan;
	private Timestamp createdAt;
	private String jenis;
	
	public Stok(Stok stok) {
		this.id = stok.id;
		this.tanggal = stok.tanggal;
		this.keterangan = stok.keterangan;
		this.qtyBeli = stok.qtyBeli;
		this.hargaBeli = stok.hargaBeli;
		this.qtyJual = stok.qtyJual;
		this.hargaJual = stok.hargaJual;
//		this.stokDetails = stok.stokDetails;
	}
	
	

	public Stok(Date tanggal, String faktur, String keterangan, Double qtyBeli, Double hargaBeli, Double qtyJual, Double hargaJual,
			Persediaan persediaan, Timestamp createdAt, String jenis) {
		this.tanggal = tanggal;
		this.faktur = faktur;
		this.keterangan = keterangan;
		this.qtyBeli = qtyBeli;
		this.hargaBeli = hargaBeli;
		this.qtyJual = qtyJual;
		this.hargaJual = hargaJual;
		this.persediaan = persediaan;
		this.createdAt = createdAt;
		this.jenis = jenis;
	}

//	@Transient
//	public void addStokDetails(StokDetails stokDetails) {
//		this.stokDetails.add(stokDetails);
//	}
//	
//	@Transient
//	public void deleteStokDetails(int index) {
//		this.stokDetails.remove(index);
//	}
	

}