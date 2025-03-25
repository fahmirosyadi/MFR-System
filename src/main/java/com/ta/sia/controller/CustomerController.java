//package com.ta.sia.controller;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.ui.Model;
//import com.ta.sia.repository.CustomerRepository;
//import com.ta.sia.repository.UserRepository;
//import com.ta.sia.entity.Customer;
//import com.ta.sia.entity.Supplier;
//
//import java.util.List;
//import java.security.Principal;
//
//@Controller
//@RequestMapping("/customer")
//public class CustomerController {
//
//	@Autowired
//	UserRepository ur;
//
//	@GetMapping("")
//	public String index(Model model, Principal p) {
//		model.addAttribute("title", "Customer");
//		model.addAttribute("user", ur.findByUsername(p.getName()).get());
//		return "corona/customer";
//	}
//	
//	@GetMapping("/{customer}/piutang")
//	public String utang(Model model, Principal p,@PathVariable Customer customer) {
//		model.addAttribute("menu", "Customer");
//		model.addAttribute("submenu", "Buku Pembantu Piutang");
//		model.addAttribute("user", ur.findByUsername(p.getName()).get());
//		model.addAttribute("customer", customer);
//		return "corona/buku_pembantu_piutang";
//	}
//}