package com.ta.sia.entity;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.ta.sia.config.View.All;

import lombok.Data;
import lombok.Getter;


@Getter
@Entity
@Immutable
@Subselect("select * from jurnal_view")
public class JurnalView {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonView(All.class)
	private Long id;
	@JsonView(All.class)
	private Date tanggal;
	@JsonView(All.class)
	private String keterangan;
	@JsonView(All.class)
	private String jenis;
	private Timestamp createdAt = new Timestamp(System.currentTimeMillis());
	@JsonView(All.class)
	private String faktur;
	@JsonView(All.class)
	@OneToMany(mappedBy = "jurnalView")
	@JsonIgnoreProperties({"jurnalView"})
	private List<JurnalViewDetails> jurnalViewDetails;
	
	@OneToOne
	@JoinColumn(name = "id")
	@JsonIgnore
	private Transaksi transaksi;
	
}
