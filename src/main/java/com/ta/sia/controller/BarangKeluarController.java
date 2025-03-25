//package com.ta.sia.controller;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Sort;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.validation.annotation.Validated;
//
//import com.ta.sia.repository.JurnalDetailsRepository;
//import com.ta.sia.repository.JurnalRepository;
//import com.ta.sia.repository.BarangKeluarRepository;
//import com.ta.sia.repository.PersediaanRepository;
//import com.ta.sia.repository.RekeningRepository;
//import com.ta.sia.repository.UserRepository;
//import com.ta.sia.entity.BarangKeluar;
//import com.ta.sia.entity.BarangKeluarDetails;
//import com.ta.sia.entity.Rekening;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.security.Principal;
//
//@Controller
//@RequestMapping("/barang_keluar")
//public class BarangKeluarController {
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
//	BarangKeluarRepository ppr;
//
//	@GetMapping("")
//	public String index(Model model, Principal p) {
//		model.addAttribute("menu", "Persediaan");
//		model.addAttribute("submenu", "Barang Keluar");
//		model.addAttribute("barangKeluar", ppr.findAll(Sort.by("createdAt")));
//		model.addAttribute("user", ur.findByUsername(p.getName()).get());
//		return "corona/barang_keluar";
//	}
//	
//	@PostMapping("")
//	public String save(@Validated BarangKeluar barangKeluar, BindingResult bindingResult){
//		if (bindingResult.hasErrors()) {
////			List<String> errors = bindingResult.getAllErrors().stream()
////			.map(e -> e.getDefaultMessage())
////			.collect(Collectors.toList());
//		}else{
//			if(barangKeluar.getId() != null) {
//				ppr.deleteById(barangKeluar.getId());
//			}
//			barangKeluar.detailsFilter();
//			ppr.save(barangKeluar);
//		}
//		return "redirect:/barang_keluar";
//	}
//	
//	@GetMapping("/create")
//	public String create(Model model, Principal p) {
//		model.addAttribute("title", "Barang Keluar Entry");
//		model.addAttribute("barang", pr.findAll());
//		BarangKeluar barangKeluar = new BarangKeluar();
//		barangKeluar.setBarangKeluarDetails(new ArrayList<BarangKeluarDetails>());
//		for(int i = 0; i < 10; i++) {
//			barangKeluar.getBarangKeluarDetails().add(null);
//		}
//		model.addAttribute("barangKeluar", barangKeluar);
//		List<Rekening> result = new ArrayList<Rekening>();
//		for(Rekening rek : rr.findAll()) {
//			if(rek.isHeader() == false) {
//				result.add(rek);
//			}
//		}
//		model.addAttribute("rekening", result);
//		model.addAttribute("user", ur.findByUsername(p.getName()).get());
//		return "corona/barang_keluar_entry";
//	}
//	
//	@GetMapping("/edit/{bk}")
//	public String edit(Model model, Principal p,@PathVariable BarangKeluar bk) {
//		model.addAttribute("title", "Barang Keluar Entry");
//		model.addAttribute("barang", pr.findAll());
//		for(int i = bk.getBarangKeluarDetails().size(); i < 10; i++) {
//			bk.getBarangKeluarDetails().add(null);
//		}
//		model.addAttribute("barangKeluar", bk);
//		List<Rekening> result = new ArrayList<Rekening>();
//		for(Rekening rek : rr.findAll()) {
//			if(rek.isHeader() == false) {
//				result.add(rek);
//			}
//		}
//		model.addAttribute("rekening", result);
//		model.addAttribute("user", ur.findByUsername(p.getName()).get());
//		return "corona/barang_keluar_entry";
//	}
//	
//	@GetMapping("delete/{id}")
//	public String delete(@PathVariable Long id) {
//		ppr.deleteById(id);
//		return "redirect:/barang_keluar";
//	}
//	
//}