//package com.ta.sia.controller;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.ui.Model;
//import com.ta.sia.repository.PersediaanRepository;
//import com.ta.sia.repository.UserRepository;
//import com.ta.sia.entity.Persediaan;
//import java.util.List;
//import java.security.Principal;
//
//@Controller
//public class PersediaanController {
//
//	@Autowired
//	UserRepository ur;
//
//	@RequestMapping(value = "/persediaan", method = RequestMethod.GET)
//	public String index(Model model, Principal p) {
//		model.addAttribute("menu", "Persediaan");
//		model.addAttribute("submenu", "");
//		model.addAttribute("user", ur.findByUsername(p.getName()).get());
//		return "corona/persediaan";
//	}
//}