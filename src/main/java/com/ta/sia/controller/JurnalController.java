//package com.ta.sia.controller;
//
//import java.security.Principal;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.validation.Valid;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import com.ta.sia.entity.BarangKeluar;
//import com.ta.sia.entity.Counter;
//import com.ta.sia.entity.Jurnal;
//import com.ta.sia.entity.JurnalDetails;
//import com.ta.sia.entity.Pembelian;
//import com.ta.sia.entity.Penjualan;
//import com.ta.sia.entity.BarangMasuk;
//import com.ta.sia.entity.CetakJurnal;
//import com.ta.sia.entity.Rekening;
//import com.ta.sia.repository.AccountLinkRepository;
//import com.ta.sia.repository.BarangKeluarRepository;
//import com.ta.sia.repository.JurnalDetailsRepository;
//import com.ta.sia.repository.JurnalRepository;
//import com.ta.sia.repository.PembelianRepository;
//import com.ta.sia.repository.PenjualanRepository;
//import com.ta.sia.repository.BarangMasukRepository;
//import com.ta.sia.repository.RekeningRepository;
//import com.ta.sia.repository.UserRepository;
//
//@Controller
//@RequestMapping("/jurnal")
//public class JurnalController {
//	
//	@Autowired
//	UserRepository ur;
//	@Autowired
//	RekeningRepository rr;
//	@Autowired
//	JurnalDetailsRepository jdr;
//	@Autowired
//	JurnalRepository jr;
//	@Autowired
//	PembelianRepository  pr;
//	@Autowired
//	BarangMasukRepository  ppr;
//	@Autowired
//	BarangKeluarRepository  bkr;
//	@Autowired
//	PenjualanRepository pnr;
//	@Autowired 
//	AccountLinkRepository alr;
//	
//	
//	@GetMapping("")
//	public String index(Model model, Principal p) {
//		CetakJurnal cj = new CetakJurnal();
//		for(Jurnal j : jr.findAll()) {
//			cj.addTransaksi(j);
//		}
//		for(Pembelian pem : pr.findAll()) {
//			cj.addTransaksi(pem);
//		}
//		for(Penjualan pen : pnr.findAll()) {
//			cj.addTransaksi(pen);
//		}
//		for(BarangMasuk pp : ppr.findAll()) {
//			cj.addTransaksi(pp);
//		}
//		for(BarangKeluar pp : bkr.findAll()) {
//			cj.addTransaksi(pp);
//		}
//		
//		model.addAttribute("menu", "Transaksi");
//		model.addAttribute("submenu", "Jurnal");
//		model.addAttribute("jurnalDetails", cj.getResult());
//		List<Rekening> result2 = new ArrayList<Rekening>();
//		for(Rekening rek : rr.findAll()) {
//			if(rek.isHeader() == false) {
//				result2.add(rek);
//			}
//		}
//		model.addAttribute("rekening", result2);
//		model.addAttribute("user", ur.findByUsername(p.getName()).get());
//		model.addAttribute("counter", new Counter());
//		return "corona/jurnal"; 
//	}
//	
//	@PostMapping("")
//	public String save(@Valid Jurnal jurnal, BindingResult bindingResult, RedirectAttributes ra, HttpServletRequest request){
//		if (bindingResult.hasErrors()) {
//			List<String> errors = bindingResult.getAllErrors().stream()
//				.map(e -> e.getDefaultMessage())
//				.collect(Collectors.toList());
//			ra.addFlashAttribute("warning", errors.get(0));
//			return "redirect:" + request.getHeader("Referer");
//		}else{
//			if(jurnal.getJurnalDetails().size() <= 0) {
//				ra.addFlashAttribute("warning", "Jurnal harus mempunyai paling sedikit 1 detail!");
//				return "redirect:" + request.getHeader("Referer");
//			}else {
//				if(jurnal.getTotal() != 0) {
//					ra.addFlashAttribute("warning", "Jurnal tidak balance!!");
//					return "redirect:" + request.getHeader("Referer");
//				}else {
//					jr.save(jurnal);
//					ra.addFlashAttribute("message", "Data berhasil disimpan!!");
//				}
//			}
//		}
//		return "redirect:/jurnal";
//	}
//	
//	@GetMapping("delete/{id}")
//	public String delete(@PathVariable Long id, RedirectAttributes ra) {
//		jr.deleteById(id);
//		ra.addFlashAttribute("message", "Data berhasil dihapus!");
//		return "redirect:/jurnal";
//	}
//	
//	@GetMapping("/create")
//	public String create(Model model, Principal p) {
//		model.addAttribute("title", "jurnal_entry");
//		model.addAttribute("jurnal", new Jurnal());
//		List<Rekening> result = new ArrayList<Rekening>();
//		for(Rekening rek : rr.findAll()) {
//			if(rek.isHeader() == false) {
//				result.add(rek);
//			}
//		}
//		model.addAttribute("rekening", result);
//		model.addAttribute("user", ur.findByUsername(p.getName()).get());
//		return "corona/jurnal_entry";
//	}
//	
//	@GetMapping("/edit/{id}")
//	public String edit(Model model, Principal p, @PathVariable Long id) {
//		model.addAttribute("title", "jurnal_entry");
//		model.addAttribute("jurnal", jr.findById(id));
//		List<Rekening> result = new ArrayList<Rekening>();
//		for(Rekening rek : rr.findAll()) {
//			if(rek.isHeader() == false) {
//				result.add(rek);
//			}
//		}
//		model.addAttribute("rekening", result);
//		model.addAttribute("user", ur.findByUsername(p.getName()).get());
//		return "corona/jurnal_entry";
//	}
//	
//}
