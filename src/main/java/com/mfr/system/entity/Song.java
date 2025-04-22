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
public class Song extends AbstractEntity{

	@NotBlank(message = "Song tidak boleh kosong!")
	@JsonView(All.class)
	@Orderable
	private String song;
	private Integer highestPitch;
	private Integer lowestPitch;
	@ManyToOne
	private Genre genre;

	public Song(Long id, String song) {
		this.id = id;
		this.song = song;
	}
	
	@Override
	public String getLabel() {
		return this.song;
	}

	@Override
	public Long getValue() {
		return this.id;
	}

}
