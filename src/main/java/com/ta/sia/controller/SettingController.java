package com.ta.sia.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ta.sia.entity.AccountLink;
import com.ta.sia.entity.SettingUmum;
import com.ta.sia.repository.AccountLinkRepository;
import com.ta.sia.repository.RekeningRepository;
import com.ta.sia.repository.SettingUmumRepository;
import com.ta.sia.repository.UserRepository;

@Controller
@RequestMapping("/setting")
public class SettingController {
	
	@Autowired
	UserRepository ur;
	@Autowired
	RekeningRepository rr;
	@Autowired
	AccountLinkRepository alr;
	@Autowired
	SettingUmumRepository sur;
	
	@GetMapping("")
	public String index(Model model, Principal p) {
		model.addAttribute("menu", "Setting");
		model.addAttribute("submenu", "Setting Umum");
		model.addAttribute("user", ur.findByUsername(p.getName()).get());
		model.addAttribute("setting", sur.findById(Long.valueOf(1)).get());
		return "corona/setting_umum";
	}
	
	@GetMapping("/rekening")
	public String rekening(Model model, Principal p) {
		model.addAttribute("menu", "Setting");
		model.addAttribute("submenu", "Setting Rekening");
		model.addAttribute("user", ur.findByUsername(p.getName()).get());
		model.addAttribute("setting", alr.findById(Long.valueOf(1)).get());
		model.addAttribute("rekening", rr.findByChild(null));
		return "corona/setting_rekening";
	}
	
	@PostMapping("rekening")
	public String saveRekening(AccountLink setting, RedirectAttributes ra) {
		alr.save(setting);
		ra.addFlashAttribute("message", "Data berhasil disimpan!");
		return "redirect:/setting/rekening";
	}
	
	@PostMapping("")
	public String saveUmum(SettingUmum setting, RedirectAttributes ra) {
		sur.save(setting);
		ra.addFlashAttribute("message", "Data berhasil disimpan!");
		return "redirect:/setting";
	}
}
