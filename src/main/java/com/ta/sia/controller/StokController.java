package com.ta.sia.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import com.ta.sia.repository.PersediaanRepository;
import com.ta.sia.repository.UserRepository;
import com.ta.sia.entity.Persediaan;
import com.ta.sia.entity.Stok;

import java.util.List;
import java.security.Principal;

@Controller
public class StokController {

	@Autowired
	UserRepository ur;
	
	@Autowired
	PersediaanRepository pr;

	@RequestMapping(value = "/stok/{id}", method = RequestMethod.GET)
	public String index(Model model, Principal p, @PathVariable Long id) {
		model.addAttribute("title", "Stok");
		model.addAttribute("persediaan", id);
		Persediaan persediaan = pr.findById(id).get();
		List<Stok> stok = persediaan.getStok();
		model.addAttribute("fifo", stok);
		model.addAttribute("user", ur.findByUsername(p.getName()).get());
		return "corona/stok";
	}
}