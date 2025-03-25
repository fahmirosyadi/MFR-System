package com.ta.sia.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.ta.sia.config.View;
import com.ta.sia.validation.MustHasAnId;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor
public class Rekening extends AbstractEntity implements Tree<Rekening>{
	
	@JsonView(View.All.class)
	private String nomor;
	
	@JsonView(View.All.class)
	@NotBlank(message = "Nama Rekening tidak boleh kosong")
	@Column(name = "rekening")
	private String nama;
	
	@JsonView(View.All.class)
	private String jenis = "";
	
	@Transient
	private Double saldo = Double.valueOf(0);
	
	@JsonView(View.All.class)
	@Transient
	private Double saldoPeriodik = Double.valueOf(0);
	
	@JsonView(View.All.class)
	private Double saldoAwal = Double.valueOf(0);
	
	@JsonView(View.All.class)
	private boolean defaultAccount = false;
	
	@JsonView(View.All.class)
	private Boolean kontra = false;
	
	@JsonView(View.All.class)
	@Transient
	private Date periode = null;
	

	@JsonView(View.All.class)
	@Transient
	private Date startPeriode;
	
	@JsonIgnore
	@OneToOne(mappedBy = "rekLabaTahunBerjalan")
	private AccountLink rekLabaTahunBerjalan;
	
	@JsonIgnore
	@OneToOne(mappedBy = "rekLabaDitahan")
	private AccountLink rekLabaDitahan;
	
	@JsonIgnoreProperties(value = {"child","parent"})
	@ManyToOne
	@JsonView(View.All.class)
	private Rekening parent;
	
	@OneToMany(mappedBy = "rekening",fetch = FetchType.LAZY)
	@JsonIgnoreProperties(value = {"rekening"})
	@OrderBy(value = "jurnalView")
	private List<JurnalViewDetails> jurnalViewDetails;

	
	@OneToMany(mappedBy = "parent",cascade = CascadeType.ALL)
	@OrderBy(value = "id")
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Rekening> child = new ArrayList<Rekening>();
	
	@Transient
	@Setter @Getter
	private static List<Transaksi> transaksi = new ArrayList<Transaksi>();

	public Rekening(String nomor, String nama, String jenis, Double saldoAwal, boolean defaultAccount,
			Rekening parent) {
		this.nomor = nomor;
		this.nama = nama;
		this.jenis = jenis;
		this.saldoAwal = saldoAwal;
		this.defaultAccount = defaultAccount;
		this.parent = parent;
		if(parent != null)parent.child.add(this);
	}

	public String getNomorBelakang() {
		return this.getNomor().substring(2);
	}
	
	@Transient
	public boolean isHeader(){
		if (this.child.size() > 0) {
			return true;
		}else {
			return false;
		}
	}

	@Transient
	public Date getPeriode() {
		if(this.periode == null && this.parent != null) {
			return this.parent.getPeriode();
		}
		if(this.periode == null) {
			this.periode = new Date(System.currentTimeMillis());
		}
		return periode;
	}

	public Date getStartPeriode() {
		if(this.startPeriode == null && this.parent != null) {
			return this.parent.getStartPeriode();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(this.getPeriode().getTime());
		cal.set(cal.get(Calendar.YEAR), 0, 1, 0, 0, 0);
		return new Date(cal.getTimeInMillis());
	}
	
	@JsonView(View.All.class)
	@Transient
	public Date getAkhirPeriode() {
		Calendar cal = Calendar.getInstance();
		if(this.getPeriode() != null) {
			cal.setTime(this.getPeriode());
			cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) - 1);
			Date akhirPeriode = new Date(cal.getTimeInMillis());
			return akhirPeriode;
		}
		return null;
	}
	
	
	private static int getLastDateOfMonth(int month, int year) {
		switch(month) {
			case 4:
			case 6:
			case 9:
			case 11:
				return 30;
			case 2:
				if(year % 400 == 0 || (year % 4 == 0 && year % 100 != 0)) {
					return 29;
				}else {
					return 28;
				}
			default :
				return 31;
		}
	}
	
	@JsonIgnore
	public Double getJurnalAmount(JurnalViewDetails jd) {
		Double s = Double.valueOf(0);
		if(this.jenis.equals("assets") || this.jenis.equals("beban") || this.jenis.equals("hpp")) {
			s += !this.kontra ? jd.getDebet() - jd.getKredit() : jd.getDebet() - jd.getKredit();
		}else if(this.jenis.equals("utang") || this.jenis.equals("modal") || this.jenis.equals("pendapatan")){
			s += !this.kontra ? jd.getKredit() - jd.getDebet() : jd.getKredit() - jd.getDebet();
		}
		return s;
	}
	
	public Double getSaldo() {
		this.saldo = Double.valueOf(0);
		if(this.isHeader()) {
			if(this.jenis.equals("labarugi")) {
				for(Rekening r : this.child) {
					if(r.jenis.equals("pendapatan")) {
						saldo += r.getSaldo();
					}else if(r.jenis.equals("beban") || r.jenis.equals("hpp")) {
						saldo -= r.getSaldo();
					}
				}
			}else {
				for(Rekening r : this.child) {
					saldo += r.getSaldo();
				}
			}
			
		}else {
			if(this.jurnalViewDetails != null && this.getPeriode() != null) {
				for(JurnalViewDetails jd : this.jurnalViewDetails) {
					if(jd.getJurnalView().getTanggal().compareTo(this.getPeriode()) <= 0) {
						saldo += this.getJurnalAmount(jd);
					}
				}
			}
		}
		return this.saldoAwal + this.saldo;
	}
	
	public Double getSaldoPeriodik() {
		this.saldoPeriodik = Double.valueOf(0);
		if(this.jenis.equals("labarugi")) {
			for(Rekening r : this.child) {
				if(r.jenis.equals("pendapatan")) {
					saldoPeriodik += r.getSaldoPeriodik();
				}else if(r.jenis.equals("beban") || r.jenis.equals("hpp")) {
					saldoPeriodik -= r.getSaldoPeriodik();
				}
			}
		}else if(this.isHeader() == true) {
			for(Rekening child : this.child) {
				saldoPeriodik += child.getSaldoPeriodik();
			}
		}else {
			if(this.getPeriode() != null && this.jurnalViewDetails != null) {
				int i = 0;
				for(JurnalViewDetails jd : this.jurnalViewDetails) {
					if(jd.getJurnalView().getTanggal() != null && this.getStartPeriode() != null && this.getPeriode() != null) {
						if(jd.getJurnalView().getTanggal().compareTo(this.getStartPeriode()) >= 0 && jd.getJurnalView().getTanggal().compareTo(this.getPeriode()) <= 0) {
							saldoPeriodik += this.getJurnalAmount(jd);
						}
					}
				}
			}
		}
		return this.saldoAwal + saldoPeriodik;
	}

	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return this.getNama();
	}

	@Override
	public Long getValue() {
		// TODO Auto-generated method stub
		return this.getId();
	}

}