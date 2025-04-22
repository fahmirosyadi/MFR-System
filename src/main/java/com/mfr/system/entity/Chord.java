package com.mfr.system.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.mfr.system.annotation.Orderable;
import com.mfr.system.config.View.All;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
@Entity
public class Chord extends AbstractEntity{

	@JsonView(All.class)
	@Orderable
	private String chord;
	private String lyric;
	@ManyToOne
	private SongPart songPart;

	public Chord(Long id, String chord) {
		this.id = id;
		this.chord = chord;
	}
	
	@Override
	public String getLabel() {
		return this.chord;
	}

	@Override
	public Long getValue() {
		return this.id;
	}

}
