package com.mfr.system.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.transaction.Transactional;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.mfr.system.annotation.Orderable;
import com.mfr.system.config.View.All;
import com.mfr.system.config.View.Detail;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.stream.Collectors;


@SuppressWarnings("serial")
@Entity
@Table(name = "users")
@Transactional
@Setter @Getter @ToString
public class User extends AbstractEntity implements UserDetails{

	@JsonView(All.class)
	@Orderable
	private String nama;
	@Column(unique = true)
	@JsonView(All.class)
	private String username;
	@Column(name = "pass")
	private String password;
	@ManyToMany(fetch = FetchType.EAGER)
	@JsonIgnoreProperties(value = "user")
	@JsonView(All.class)
	private List<Role> role = new ArrayList<Role>();
	@Column(name = "active")
	@JsonView(All.class)
	private boolean enabled;
	@Orderable
	@JsonView(All.class)
	private String email;
	@Transient
	private boolean changePassword = false;
	@OneToOne(mappedBy = "user")
	@JsonIgnoreProperties({"user"})
	private PasswordResetToken passwordResetToken;
	@Transient
	private String passwordConfirm;
	
	@Transient
	@JsonIgnore
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.getRole()
		.stream().map(role-> new SimpleGrantedAuthority("ROLE_"+role.getRole()))
		.collect(Collectors.toList());
	}
	
	@Transient
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Transient
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Transient
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	@Transient
	public List<Menu> getMenu() {
		List<Menu> result = new ArrayList<>();
		for(Role role : this.getRole()) {
			for(Menu m : role.getMenu()) {
				if(!result.contains(m)) {
					result.add(m);
				}
			}
		}
		return result;
	}

	public Object getRootMenu() {
		List<Menu> result = new ArrayList<>();
		for(Menu m : this.getMenu()) {
			for(Menu child : m.getChild()) {
				Boolean status = false;
				
				for(Menu mn : this.getMenu()) {
					if(child.getId() == mn.getId()) {
						status = true;
					}
				}
				if(!status) {
					child.setAccessible(false);
				}
			}
			if(m.getParent() == null) {
				result.add(m);
			}
		}
		System.out.println("Size : " + result.size());
		return result;
	}

	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return this.username;
	}

	@Override
	public Long getValue() {
		// TODO Auto-generated method stub
		return this.id;
	}

}
