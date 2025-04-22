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
public class SongPart extends AbstractEntity{

	@NotBlank(message = "Part tidak boleh kosong!")
	@JsonView(All.class)
	@Orderable
	private String part;
	private String chord;
	@ManyToOne
	private Song song;

	public SongPart(Long id, String part) {
		this.id = id;
		this.part = part;
	}
	
	@Override
	public String getLabel() {
		return this.part;
	}

	@Override
	public Long getValue() {
		return this.id;
	}

}
