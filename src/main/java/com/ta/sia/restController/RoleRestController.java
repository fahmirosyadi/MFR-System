package com.ta.sia.restController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ta.sia.entity.Role;
import com.ta.sia.repository.CommonRepository;
import com.ta.sia.repository.RoleRepository;

@RestController
@CrossOrigin
@RequestMapping("/api/role")
public class RoleRestController extends AbstractRestController<Role> {

	@Autowired
	private RoleRepository rr;
	
	@Override
	public CommonRepository<Role, Long> getRepository() {
		return this.rr;
	}

	@Override
	public List<String> getDatatablesFields() {
		return this.getDatatablesFields(Role.class.getDeclaredFields());
	}

}
