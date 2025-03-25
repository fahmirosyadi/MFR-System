package com.ta.sia.entity;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.type.ForeignKeyDirection;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.ta.sia.config.View.All;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Immutable
@Subselect("select * from jurnal_view_details")
public class JurnalViewDetails  {
	@Id
	@JsonView(All.class)
	protected String id;
	@ManyToOne
	@JsonView(All.class)
	@JoinColumn(name = "rekening_id")
	@JsonIgnoreProperties({"parent","child","jurnalViewDetails"})
	private Rekening rekening;
	@JsonView(All.class)
	private Double debet;
	@JsonView(All.class)
	private Double kredit;
	
	@ManyToOne
	@JoinColumn(name = "jurnal_view_id") 
	@JsonIgnoreProperties({"jurnalViewDetails"})
	JurnalView jurnalView;
}
