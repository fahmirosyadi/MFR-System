package com.mfr.system.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
public class Genre extends AbstractEntity{

	@NotBlank(message = "Genre tidak boleh kosong!")
	@JsonView(All.class)
	@Orderable
	private String genre;

	public Genre(String Genre) {
		this.genre = Genre;
	}
	
	@Override
	public String getLabel() {
		return this.genre;
	}

	@Override
	public Long getValue() {
		return this.id;
	}

}
