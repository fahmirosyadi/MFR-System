package com.mfr.system.service;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mfr.system.entity.Menu;
import com.mfr.system.entity.Role;
import com.mfr.system.repository.MenuRepository;
import com.mfr.system.repository.RoleRepository;


@Service
public class MenuService {

	@Autowired
	MenuRepository mr;
	@Autowired
	RoleRepository rr;
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public Menu getMenuWithAuthority(Menu entity) {
		Menu existing = this.mr.findById(entity.getId()).get();
		for(Role r : existing.getRole()) {
			r.getMenu().remove(existing);
		}
		existing.setRole(null);
		existing.setRole(new ArrayList<Role>());
		for(Role r : entity.getRole()) {
			Role role = this.rr.findById(r.getId()).get();
			role.getMenu().add(existing);
			existing.getRole().add(role);
		}
		return existing;
	}
	
}
