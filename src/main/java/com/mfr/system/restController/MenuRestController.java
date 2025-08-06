package com.mfr.system.restController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mfr.system.entity.Menu;
import com.mfr.system.entity.Role;
import com.mfr.system.entity.TreeUtil;
import com.mfr.system.entity.User;
import com.mfr.system.repository.CommonRepository;
import com.mfr.system.repository.MenuRepository;
import com.mfr.system.repository.RoleRepository;
import com.mfr.system.repository.UserRepository;

@RestController
@CrossOrigin
@RequestMapping("/api/menu")
public class MenuRestController  extends AbstractRestController<Menu>{
	
	@Autowired
	MenuRepository mr;
	@Autowired
	UserRepository ur;
	@Autowired
	RoleRepository rr;

	@GetMapping("/user")
	@Override
	public ResponseEntity<Object> index(Principal p){		
		System.out.println("Tess");
		Optional<User> user = ur.findByUsername(p.getName(),Sort.by("role.menu.sort"));
		Map<String,Object> obj = new HashMap<String, Object>();
		if(user.isPresent()) {	
//			List<Menu> data = mr.findLike(search, p.getName());	
			obj.put("status", true);
			obj.put("messages", "Data berhasil diambil");
			obj.put("data", user.get().getRootMenu());
		}
			
		return new ResponseEntity<>(obj,HttpStatus.OK);
	}

	@GetMapping("/search")	
	public ResponseEntity<Object> toList(){
		Map<String,Object> obj = new HashMap<String, Object>();
		obj.put("status", true);
		obj.put("messages", "Data berhasil diambil");
		this.mr.findByParent(null);
		obj.put("data", TreeUtil.toList(this.mr.findByParent(null), 0));
		return new ResponseEntity<>(obj,HttpStatus.OK);
	}

	@GetMapping("")
	public ResponseEntity<Object> search(@RequestParam(name = "search", defaultValue = "") String search, Principal p){		
		Map<String,Object> obj = new HashMap<String, Object>();
		List<Menu> data = mr.findLike(search);	
		obj.put("status", true);
		obj.put("messages", "Data berhasil diambil");
		obj.put("data", data);
			
		return new ResponseEntity<>(obj,HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<Object> save(@RequestBody Menu entity, BindingResult bindingResult) {
		System.out.println("Roles : " + entity.getRole().size());
		Menu existing = this.mr.findById(entity.getId()).get();
		for(Role r : existing.getRole()) {
			r.getMenu().remove(existing);
		}
		existing.setRole(new ArrayList<Role>());
		for(Role r : entity.getRole()) {
			Role role = this.rr.findById(r.getId()).get();
			role.getMenu().add(existing);
			existing.getRole().add(role);
		}
		System.out.println("Menu role : " + existing.getRole().size());
		return super.save(existing, bindingResult);
	}

	@Override
	public CommonRepository<Menu, Long> getRepository() {
		return this.mr;
	}
	
	@Override
	public List<String> getDatatablesFields() {
		return this.getDatatablesFields(Menu.class.getDeclaredFields());
	}
	
}
