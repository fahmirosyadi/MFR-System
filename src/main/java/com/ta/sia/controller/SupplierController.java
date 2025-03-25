package com.ta.sia.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import com.ta.sia.repository.SupplierRepository;
import com.ta.sia.repository.UserRepository;
import com.ta.sia.entity.Supplier;
import java.security.Principal;

@Controller
@RequestMapping("/supplier")
public class SupplierController {

	@Autowired
	UserRepository ur;
	
	@Autowired
	SupplierRepository sr;

	@GetMapping("")
	public String index(Model model, Principal p) {
		model.addAttribute("title", "Supplier");
		model.addAttribute("user", ur.findByUsername(p.getName()).get());
		return "corona/supplier";
	}
	
	@GetMapping("/{supplier}/utang")
	public String utang(Model model, Principal p,@PathVariable Supplier supplier) {
		model.addAttribute("menu", "Supplier");
		model.addAttribute("submenu", "Buku Pembantu Utang");
		model.addAttribute("user", ur.findByUsername(p.getName()).get());
		model.addAttribute("supplier", supplier);
		return "corona/buku_pembantu_utang";
	}
	
}