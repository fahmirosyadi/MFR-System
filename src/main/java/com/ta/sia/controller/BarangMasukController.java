//package com.ta.sia.controller;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
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
//import com.ta.sia.repository.BarangMasukRepository;
//import com.ta.sia.repository.PersediaanRepository;
//import com.ta.sia.repository.RekeningRepository;
//import com.ta.sia.repository.SupplierRepository;
//import com.ta.sia.repository.UserRepository;
//import com.ta.sia.entity.AccountLink;
//import com.ta.sia.entity.Jurnal;
//import com.ta.sia.entity.JurnalDetails;
//import com.ta.sia.entity.Pembelian;
//import com.ta.sia.entity.PembelianDetails;
//import com.ta.sia.entity.BarangMasuk;
//import com.ta.sia.entity.BarangMasukDetails;
//import com.ta.sia.entity.Rekening;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//import java.security.Principal;
//
//@Controller
//@RequestMapping("/barang_masuk")
//public class BarangMasukController {
//
//	@Autowired
//	UserRepository ur;
//	
//	@Autowired
//	JurnalRepository jr;
//	@Autowired
//	JurnalDetailsRepository jdr;
//	
//	@Autowired
//	RekeningRepository rr;
//	
//	@Autowired
//	PersediaanRepository pr;
//	
//	@Autowired
//	BarangMasukRepository ppr;
//
//	@GetMapping("")
//	public String index(Model model, Principal p) {
//		model.addAttribute("menu", "Persediaan");
//		model.addAttribute("submenu", "Barang Masuk");
//		model.addAttribute("barangMasuk", ppr.findAll(Sort.by("createdAt")));
//		model.addAttribute("user", ur.findByUsername(p.getName()).get());
//		return "corona/barang_masuk";
//	}
//	
//	@PostMapping("")
//	public String save(@Validated BarangMasuk barangMasuk, BindingResult bindingResult){
//		if (bindingResult.hasErrors()) {
//			List<String> errors = bindingResult.getAllErrors().stream()
//			.map(e -> e.getDefaultMessage())
//			.collect(Collectors.toList());
//		}else{
//			if(barangMasuk.getId() != null) {
//				ppr.deleteById(barangMasuk.getId());
//			}
//			barangMasuk.detailsFilter();
//			ppr.save(barangMasuk);
//		}
//		return "redirect:/barang_masuk";
//	}
//	
//	@GetMapping("/create")
//	public String create(Model model, Principal p) {
//		model.addAttribute("title", "Barang Masuk Entry");
//		model.addAttribute("barang", pr.findAll());
//		BarangMasuk barangMasuk = new BarangMasuk();
//		barangMasuk.setBarangMasukDetails(new ArrayList<BarangMasukDetails>());
//		for(int i = 0; i < 10; i++) {
//			barangMasuk.getBarangMasukDetails().add(null);
//		}
//		model.addAttribute("barangMasuk", barangMasuk);
//		List<Rekening> result = new ArrayList<Rekening>();
//		for(Rekening rek : rr.findAll()) {
//			if(rek.isHeader() == false) {
//				result.add(rek);
//			}
//		}
//		model.addAttribute("rekening", result);
//		model.addAttribute("user", ur.findByUsername(p.getName()).get());
//		return "corona/barang_masuk_entry";
//	}
//	
//	@GetMapping("/edit/{bm}")
//	public String edit(Model model, Principal p,@PathVariable BarangMasuk bm) {
//		model.addAttribute("title", "Barang Masuk Entry");
//		model.addAttribute("barang", pr.findAll());
//		for(int i = bm.getBarangMasukDetails().size(); i < 10; i++) {
//			bm.getBarangMasukDetails().add(null);
//		}
//		model.addAttribute("barangMasuk", bm);
//		List<Rekening> result = new ArrayList<Rekening>();
//		for(Rekening rek : rr.findAll()) {
//			if(rek.isHeader() == false) {
//				result.add(rek);
//			}
//		}
//		model.addAttribute("rekening", result);
//		model.addAttribute("user", ur.findByUsername(p.getName()).get());
//		return "corona/barang_masuk_entry";
//	}
//	
//	@GetMapping("delete/{id}")
//	public String delete(@PathVariable Long id) {
//		ppr.deleteById(id);
//		return "redirect:/barang_masuk";
//	}
//	
//}