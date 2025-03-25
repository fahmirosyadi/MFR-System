//package com.ta.sia.controller;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Sort;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.validation.annotation.Validated;
//
//import com.ta.sia.repository.AccountLinkRepository;
//import com.ta.sia.repository.JurnalDetailsRepository;
//import com.ta.sia.repository.JurnalRepository;
//import com.ta.sia.repository.PembelianRepository;
//import com.ta.sia.repository.PersediaanRepository;
//import com.ta.sia.repository.RekeningRepository;
//import com.ta.sia.repository.SupplierRepository;
//import com.ta.sia.repository.UserRepository;
//import com.ta.sia.entity.AccountLink;
//import com.ta.sia.entity.Jurnal;
//import com.ta.sia.entity.JurnalDetails;
//import com.ta.sia.entity.Pembelian;
//import com.ta.sia.entity.PembelianDetails;
//import com.ta.sia.entity.Rekening;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.validation.Valid;
//
//import java.security.Principal;
//
//@Controller
//@RequestMapping("/pembelian")
//public class PembelianController {
//
//	@Autowired
//	UserRepository ur;
//	@Autowired
//	PembelianRepository pr;
//	
//	@Autowired
//	JurnalRepository jr;
//	@Autowired
//	JurnalDetailsRepository jdr;
//	@Autowired
//	AccountLinkRepository alr;
//	
//	@Autowired
//	RekeningRepository rr;
//	@Autowired
//	SupplierRepository sr;
//	
//	@Autowired
//	PersediaanRepository prr;
//
//	@GetMapping("")
//	public String index(Model model, Principal p) {
//		model.addAttribute("menu", "Transaksi");
//		model.addAttribute("submenu", "Pembelian");
//		model.addAttribute("pembelian", pr.findAll(Sort.by("createdAt")));
//		model.addAttribute("user", ur.findByUsername(p.getName()).get());
//		return "corona/pembelian";
//	}
//	
//	@PostMapping("")
//	public String save(@Valid Pembelian pembelian, BindingResult bindingResult, RedirectAttributes ra, HttpServletRequest request){
//		if (bindingResult.hasErrors()) {
//			List<String> errors = bindingResult.getAllErrors().stream()
//			.map(e -> e.getDefaultMessage())
//			.collect(Collectors.toList());
//			ra.addFlashAttribute("warning", errors.get(0));
//			return "redirect:" + request.getHeader("Referer");
//		}else{
//			if(pembelian.getPembelianDetails().size() <= 0) {
//				ra.addFlashAttribute("warning", "Pembelian harus mempunyai paling sedikit 1 detail!");
//				return "redirect:" + request.getHeader("Referer");
//			}else {
//				AccountLink al = alr.findById(Long.valueOf(1)).get();
//				pembelian.setRekPelunasanUtang(al.getRekPelunasanUtang());
//				pembelian.setRekPembelian(al.getRekPembelian());
//				if(pembelian.getBayar() == null) {
//					pembelian.setBayar(Long.valueOf(0));
//				}
//				if(pembelian.getId() != null) {
//					pr.deleteById(pembelian.getId());
//				}
//				pr.save(pembelian);
//				ra.addFlashAttribute("message", "Data Berhasil disimpan!!");
//			}
//		}
//		return "redirect:/pembelian";
//	}
//	
//	@GetMapping("/create")
//	public String create(Model model, Principal p) {
//		model.addAttribute("title", "pembelian_entry");
//		model.addAttribute("barang", prr.findAll());
//		model.addAttribute("supplier", sr.findAll());
//		Pembelian pembelian = new Pembelian();
//		pembelian.setPembelianDetails(new ArrayList<PembelianDetails>());
//		for(int i = 0; i < 10; i++) {
//			pembelian.getPembelianDetails().add(null);
//		}
//		model.addAttribute("pembelian", pembelian);
//		List<Rekening> result = new ArrayList<Rekening>();
//		for(Rekening rek : rr.findAll()) {
//			if(rek.isHeader() == false) {
//				result.add(rek);
//			}
//		}
//		model.addAttribute("rekening", result);
//		model.addAttribute("user", ur.findByUsername(p.getName()).get());
//		return "corona/pembelian_entry";
//	}
//	
//	@GetMapping("/edit/{pembelian}")
//	public String edit(Model model, Principal p,@PathVariable Pembelian pembelian) {
//		model.addAttribute("title", "pembelian_entry");
//		model.addAttribute("barang", prr.findAll());
//		model.addAttribute("supplier", sr.findAll());
//		for(int i = pembelian.getPembelianDetails().size(); i < 10; i++) {
//			pembelian.getPembelianDetails().add(null);
//		}
//		model.addAttribute("pembelian", pembelian);
//		List<Rekening> result = new ArrayList<Rekening>();
//		for(Rekening rek : rr.findAll()) {
//			if(rek.isHeader() == false) {
//				result.add(rek);
//			}
//		}
//		model.addAttribute("rekening", result);
//		model.addAttribute("user", ur.findByUsername(p.getName()).get());
//		return "corona/pembelian_entry";
//	}
//	
//	@GetMapping("delete/{id}")
//	public String delete(@PathVariable Long id, RedirectAttributes ra) {
//		pr.deleteById(id);
//		ra.addFlashAttribute("message", "Data berhasil dihapus!");
//		return "redirect:/pembelian";
//	}
//	
//}