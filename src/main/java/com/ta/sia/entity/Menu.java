package com.ta.sia.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

import org.hibernate.annotations.Sort;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter 
@AllArgsConstructor @NoArgsConstructor
@Entity
public class Menu  extends AbstractEntity implements Tree{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String url;
	private String icon;
	private Integer sort;
	@JsonIgnoreProperties(value = {"menu"})
	@ManyToMany(mappedBy = "menu",cascade = CascadeType.ALL)
	private List<Role> role = new ArrayList<>();
	@ManyToOne
	@JsonIgnore
	private Menu parent;
	@OneToMany(mappedBy = "parent",cascade = CascadeType.ALL)
	@OrderBy(value = "sort")
	private List<Menu> children = new ArrayList<Menu>();
	@Transient
	private Boolean accessible = true;

	public Menu(String name, String url, String icon, Integer sort, Menu parent) {
		this.name = name;
		this.url = url;
		this.icon = icon;
		this.sort = sort;
		this.parent = parent;
		if(this.parent != null) {
			this.parent.children.add(this);
		}
	}

	@Override
	public List<Menu> getChild() {
		return this.getChildren();
	}

	@Override
	public String getLabel() {
		return this.name;
	}

	@Override
	public Long getValue() {
		return this.id;
	}
	
	public void setRole(List<Role> role) {
		
		if(role != null) {
			System.out.println("Set role : " + role);
			for(Role r : role) {
				System.out.println("Set role 2 : " + r.getMenu());
				r.getMenu().add(this);
			}	
		}
		this.role = role;
		
	}
	
}
