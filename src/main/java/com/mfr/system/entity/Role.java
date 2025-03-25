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
public class Role extends AbstractEntity{

	@NotBlank(message = "Role tidak boleh kosong!")
	@JsonView(All.class)
	@Orderable
	private String role;
	
	@JsonIgnoreProperties(value = {"role"}, allowSetters = true, allowGetters = true)
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "authority")
	@OrderBy(value = "sort")
	private List<Menu> menu = new ArrayList<>();

	public Role(Long id, String role) {
		this.id = id;
		this.role = role;
	}
	
	@Override
	public String getLabel() {
		return this.role;
	}

	@Override
	public Long getValue() {
		return this.id;
	}

}
