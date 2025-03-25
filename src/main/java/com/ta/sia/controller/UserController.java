package com.ta.sia.controller;

import java.security.Principal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ta.sia.entity.Role;
import com.ta.sia.entity.User;
import com.ta.sia.repository.RoleRepository;
import com.ta.sia.repository.UserRepository;


@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserRepository ur;
	
	@Autowired
	RoleRepository mrr;
	
	@GetMapping("")
	public String index(Model model, Principal p) {
		model.addAttribute("title", "Pembelian");
		model.addAttribute("allUser", ur.findAll());
		model.addAttribute("user", ur.findByUsername(p.getName()).get());
		return "corona/user";
	}
	
	@GetMapping("/create")
	public String create(Model model, Principal p) {
		model.addAttribute("title", "User");
		User newUser = new User();
		newUser.setRole(new ArrayList<Role>());
		model.addAttribute("dataUser", newUser);
		model.addAttribute("roles", mrr.findAll());
		model.addAttribute("user", ur.findByUsername(p.getName()).get());
		return "corona/user_form";
	}
	
	@GetMapping("/edit/{user}")
	public String edit(Model model, Principal p, @PathVariable User user) {
		model.addAttribute("title", "User");
		model.addAttribute("dataUser", user);
		model.addAttribute("roles", mrr.findAll());
		model.addAttribute("user", ur.findByUsername(p.getName()).get());
		return "corona/user_form";
	}
	
	@GetMapping("/delete/{user}")
	public String delete(@PathVariable User user) {
		ur.delete(user);
		return "redirect:/user";
	}
	
	@PostMapping("")
	public String save(User user) {
		ur.save(user);
		return "redirect:/user";
	}
	
	
	
}
