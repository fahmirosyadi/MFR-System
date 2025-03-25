//package com.ta.sia.entity;
//
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.ManyToOne;
//
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//
//@Entity
//public class RoleUser {
//
//	private Long id;
//	private User user;
//	private String role;
//	
//	
//	public RoleUser() {
//		
//	}
//	
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	public Long getId() {
//		return this.id;
//	}
//	public void setId(Long id) {
//		this.id = id;
//	}
//	@ManyToOne
//	@JsonIgnoreProperties({"roleUser"})
//	public User getUser() {
//		return user;
//	}
//	public void setUser(User user) {
//		this.user = user;
//	}
//
//	public String getRole() {
//		return role;
//	}
//	public void setRole(String role) {
//		this.role = role;
//	}
//	
//	
//	
//}
