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
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//
//import com.ta.sia.repository.AccountLinkRepository;
//import com.ta.sia.repository.CustomerRepository;
//import com.ta.sia.repository.PenjualanRepository;
//import com.ta.sia.repository.PersediaanRepository;
//import com.ta.sia.repository.RekeningRepository;
//import com.ta.sia.repository.UserRepository;
//import com.ta.sia.entity.AccountLink;
//import com.ta.sia.entity.Penjualan;
//import com.ta.sia.entity.PenjualanDetails;
//import com.ta.sia.entity.Rekening;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.validation.Valid;
//
//import java.security.Principal;
//
//@Controller
//@RequestMapping("/penjualan")
//public class PenjualanController {
//
//	@Autowired
//	UserRepository ur;
//	@Autowired
//	PenjualanRepository pr;
//	@Autowired
//	AccountLinkRepository alr;
//	
//	@Autowired
//	PersediaanRepository prr;
//	@Autowired
//	RekeningRepository rr;
//	@Autowired
//	CustomerRepository cr;
//	
//
//	@GetMapping("")
//	public String index(Model model, Principal p) {
//		model.addAttribute("menu", "Transaksi");
//		model.addAttribute("submenu", "Penjualan");
//		model.addAttribute("penjualan", pr.findAll(Sort.by("createdAt")));
//		model.addAttribute("user", ur.findByUsername(p.getName()).get());
//		return "corona/penjualan";
//	}
//	
//	@PostMapping("")
//	public String save(@Valid Penjualan penjualan, BindingResult bindingResult, RedirectAttributes ra, HttpServletRequest request){
//		if (bindingResult.hasErrors()) {
//			List<String> errors = bindingResult.getAllErrors().stream()
//			.map(e -> e.getDefaultMessage())
//			.collect(Collectors.toList());
//			ra.addFlashAttribute("warning", errors.get(0));
//			return "redirect:" + request.getHeader("Referer");
//		}
//		AccountLink al = alr.findById(Long.valueOf(1)).get();
//		penjualan.setRekPelunasanPiutang(al.getRekPelunasanPiutang());
//		penjualan.setRekPenjualan(al.getRekPenjualan());
//
//		if(penjualan.getId() != null) {
//			pr.deleteById(penjualan.getId());
//		}
//		pr.save(penjualan); 
//		ra.addFlashAttribute("message", "Data berhasil disimpan!!");
//		return "redirect:/penjualan";
//	}
//	
//	@RequestMapping(value = "/create", method = RequestMethod.GET)
//	public String create(Model model, Principal p) {
//		model.addAttribute("title", "penjualan_entry");
//		model.addAttribute("barang", prr.findAll());
//		model.addAttribute("customer", cr.findAll());
//		Penjualan penjualan = new Penjualan();
//		penjualan.setPenjualanDetails(new ArrayList<PenjualanDetails>());
//		for(int i = 0; i < 10; i++) {
//			penjualan.getPenjualanDetails().add(null);
//		}
//		model.addAttribute("penjualan", penjualan);
//		List<Rekening> result = new ArrayList<Rekening>();
//		for(Rekening rek : rr.findAll()) {
//			if(rek.isHeader() == false) {
//				result.add(rek);
//			}
//		}
//		model.addAttribute("rekening", result);
//		model.addAttribute("user", ur.findByUsername(p.getName()).get());
//		return "corona/penjualan_entry";
//	}
//	
//	@GetMapping("/edit/{penjualan}")
//	public String edit(Model model, Principal p,@PathVariable Penjualan penjualan) {
//		model.addAttribute("title", "pembelian_entry");
//		model.addAttribute("barang", prr.findAll());
//		model.addAttribute("customer", cr.findAll());
//		for(int i = penjualan.getPenjualanDetails().size(); i < 10; i++) {
//			penjualan.getPenjualanDetails().add(null);
//		}
//		model.addAttribute("penjualan", penjualan);
//		List<Rekening> result = new ArrayList<Rekening>();
//		for(Rekening rek : rr.findAll()) {
//			if(rek.isHeader() == false) {
//				result.add(rek);
//			}
//		}
//		model.addAttribute("rekening", result);
//		model.addAttribute("user", ur.findByUsername(p.getName()).get());
//		return "corona/penjualan_entry";
//	}
//	
//	@GetMapping("delete/{id}")
//	public String delete(@PathVariable Long id, RedirectAttributes ra) {
//		pr.deleteById(id);
//		ra.addFlashAttribute("message", "Data berhasil dihapus!");
//		return "redirect:/penjualan";
//	}
//}