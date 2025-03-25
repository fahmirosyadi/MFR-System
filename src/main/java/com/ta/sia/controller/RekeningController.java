//package com.ta.sia.controller;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//
//import com.ta.sia.repository.AccountLinkRepository;
//import com.ta.sia.repository.JurnalRepository;
//import com.ta.sia.repository.PembelianRepository;
//import com.ta.sia.repository.PenjualanRepository;
//import com.ta.sia.repository.RekeningRepository;
//import com.ta.sia.repository.UserRepository;
//import com.ta.sia.entity.AccountLink;
//import com.ta.sia.entity.Jurnal;
//import com.ta.sia.entity.Pembelian;
//import com.ta.sia.entity.Penjualan;
//import com.ta.sia.entity.Persediaan;
//import com.ta.sia.entity.Rekening;
//import com.ta.sia.entity.Transaksi;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.validation.Valid;
//
//import java.security.Principal;
//import java.sql.Date;
//
//@Controller
//@RequestMapping("/rekening")
//public class RekeningController {
//
//	@Autowired
//	UserRepository ur;
//	
//	@Autowired
//	RekeningRepository rr;
//	@Autowired
//	AccountLinkRepository alr;
//	@Autowired
//	JurnalRepository jr;
//	@Autowired
//	PembelianRepository pr;
//	@Autowired
//	PenjualanRepository pnr;
//	
//	public void setTransaksi() {
//		Rekening.setTransaksi(new ArrayList<Transaksi>());
//		for(Jurnal t : jr.findAll()) {
//			Rekening.getTransaksi().add(t);
//		}
//		for(Pembelian t : pr.findAll()) {
//			Rekening.getTransaksi().add(t);
//		}
//		for(Penjualan t : pnr.findAll()) {
//			Rekening.getTransaksi().add(t);
//		}
//		System.out.println("Jumlah : " + Rekening.getTransaksi().size());
//	}
//	
//	@GetMapping("")
//	public String index(Model model, Principal p) {
//		model.addAttribute("menu", "Rekening");
//		model.addAttribute("submenu", "");
//		model.addAttribute("user", ur.findByUsername(p.getName()).get());
//		model.addAttribute("periode", new Date(System.currentTimeMillis()));
//		Rekening.setPeriode(new Date(System.currentTimeMillis()));
//		Persediaan.setPeriode(new Date(System.currentTimeMillis()));
//		Rekening.setAkhirPeriode(12);
//		setTransaksi();
//		AccountLink al = alr.findById(Long.valueOf(1)).get();
//		Rekening labaRugi = rr.findById(Long.valueOf(3)).get();
//		al.getRekLabaDitahan().setSaldo(labaRugi.getSaldo() - labaRugi.getSaldoPeriodik());
//		al.getRekLabaTahunBerjalan().setSaldo(labaRugi.getSaldoPeriodik());
//		model.addAttribute("neraca", rr.findById(Long.valueOf(2)).get());
//		model.addAttribute("labarugi", rr.findById(Long.valueOf(3)).get());
//		model.addAttribute("indent", 0);
//		return "corona/rekening";
//	}
//	
//	@GetMapping("/saldo_awal")
//	public String saldoAwal(Model model, Principal p) {
//		model.addAttribute("menu", "Rekening");
//		model.addAttribute("submenu", "Saldo Awal");
//		model.addAttribute("user", ur.findByUsername(p.getName()).get());
//		List<Rekening> result = rr.findAll();
//		for(int i = 0; i < result.size(); i++) {
//			Rekening rek = result.get(i);
//			if(rek.isHeader()) {
//				result.remove(i);
//				i--;
//			}
//		}
//		model.addAttribute("rekening", result);
//		return "corona/rekening_sa";
//	}
//	
//	@GetMapping("/create/{rekening}")
//	public String create(Model model, Principal p, @PathVariable Rekening rekening) {
//		model.addAttribute("menu", "Rekening");
//		model.addAttribute("submenu", "Create");
//		model.addAttribute("user", ur.findByUsername(p.getName()).get());
//		Rekening data = new Rekening();
//		data.setParent(rekening);
//		data.setJenis(rekening.getJenis());
//		model.addAttribute("rekening", data);
//		model.addAttribute("indent", 0);
//		return "corona/rekening_form";
//	}
//	
//	@GetMapping("/edit/{rekening}")
//	public String edit(Model model, Principal p, @PathVariable Rekening rekening) {
//		model.addAttribute("menu", "Rekening");
//		model.addAttribute("submenu", "Edit");
//		model.addAttribute("user", ur.findByUsername(p.getName()).get());
//		rekening.setNomor(rekening.getNomor().substring(2));
//		model.addAttribute("rekening", rekening);
//		model.addAttribute("indent", 0);
//		return "corona/rekening_form";
//	}
//	
//	@PostMapping("")
//	public String save(@Valid Rekening rekening,BindingResult bindingResult,  RedirectAttributes ra, HttpServletRequest request) {
//		if (bindingResult.hasErrors()) {
//			List<String> errors = bindingResult.getAllErrors().stream()
//			.map(e -> e.getDefaultMessage())
//			.collect(Collectors.toList());
//			ra.addFlashAttribute("warning", errors.get(0));
//			return "redirect:" + request.getHeader("Referer");
//		}else {
//			if(rekening.getJenis().equals("assets")) {
//				rekening.setNomor("1-" + rekening.getNomor());
//			}else if(rekening.getJenis().equals("utang")) {
//				rekening.setNomor("2-" + rekening.getNomor());
//			}else if(rekening.getJenis().equals("modal")) {
//				rekening.setNomor("3-" + rekening.getNomor());
//			}else if(rekening.getJenis().equals("pendapatan")) {
//				rekening.setNomor("4-" + rekening.getNomor());
//			}else if(rekening.getJenis().equals("hpp")) {
//				rekening.setNomor("5-" + rekening.getNomor());
//			}else if(rekening.getJenis().equals("beban")) {
//				rekening.setNomor("6-" + rekening.getNomor());
//			}
//			if(rekening.getSaldoAwal() == null) {
//				rekening.setSaldoAwal(Long.valueOf(0));
//			}
//			rr.save(rekening);
//			ra.addFlashAttribute("message","Data berhasil disimpan!!");
//		}
//		return "redirect:/rekening";
//	}
//	
//	@PostMapping("/saveAll")
//	public String saveAll(String[] id, String[] saldoAwal, RedirectAttributes ra, HttpServletRequest request) {
//		List<Rekening> lrek = new ArrayList<Rekening>();
//		Long saldoDebet = Long.valueOf(0);
//		Long saldoKredit = Long.valueOf(0);
//		for(int i = 0; i < id.length; i++) {
//			String idRek = id[i];
//			Long saldoA = Long.valueOf(0);
//			if(saldoAwal[i] != "") {
//				saldoA = Long.valueOf(saldoAwal[i]);
//			}
//			Rekening rek= rr.findById(Long.valueOf(idRek)).get();
//			rek.setSaldoAwal(saldoA);
//			if(rek.getJenis().equals("assets") || rek.getJenis().equals("beban") || rek.getJenis().equals("hpp")) {
//				saldoDebet += rek.getSaldoAwal();
//			}else {
//				saldoKredit += rek.getSaldoAwal();
//			}
//			lrek.add(rek);
//		}
//		if(saldoDebet.equals(saldoKredit) == false) {
//			ra.addFlashAttribute("warning","Saldo awal tidak balance!");
//			return "redirect:" + request.getHeader("Referer");
//		}
//		rr.saveAll(lrek);
//		ra.addFlashAttribute("message","Berhasil mengisi saldo awal!");
//		return "redirect:/rekening";
//	}
//	
//	@GetMapping("/delete/{rekening}")
//	public String edit(@PathVariable Rekening rekening, RedirectAttributes ra) {
//		AccountLink al = alr.findById(Long.valueOf(1)).get();
//		if(al.isLinked(rekening)) {
//			ra.addFlashAttribute("warning", "Rekening yang memiliki link tidak dapat dihapus!");
//		}else if(rekening.isDefaultAccount()) {
//			ra.addFlashAttribute("warning", "Rekening default tidak dapat dihapus!");
//		}else {
//			rr.delete(rekening);
//			ra.addFlashAttribute("message","Data berhasil dihapus!!");
//		}
//		return "redirect:/rekening";
//	}
//	
//}