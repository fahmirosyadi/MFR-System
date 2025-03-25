package com.ta.sia.entity;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Entity
public class SettingUmum {

	private Long id;
	private String namaPerusahaan = "";
	private Date tanggalBerdiri = new Date(System.currentTimeMillis());
//	private int akhirPeriode = 12;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@NotBlank(message = "Nama perusahaan harus diisi!")
	public String getNamaPerusahaan() {
		return namaPerusahaan;
	}
	public void setNamaPerusahaan(String namaPerusahaan) {
		this.namaPerusahaan = namaPerusahaan;
	}
	public Date getTanggalBerdiri() {
		return tanggalBerdiri;
	}
	public void setTanggalBerdiri(Date tanggalBerdiri) {
		this.tanggalBerdiri = tanggalBerdiri;
	}
//	public int getAkhirPeriode() {
//		return akhirPeriode;
//	}
//	public void setAkhirPeriode(int akhirPeriode) {
//		this.akhirPeriode = akhirPeriode;
//	}
	
}
