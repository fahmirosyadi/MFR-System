//package com.ta.sia.controller;
//
//import java.security.Principal;
//import java.sql.Date;
//import java.util.ArrayList;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.ta.sia.entity.AccountLink;
//import com.ta.sia.entity.Jurnal;
//import com.ta.sia.entity.Pembelian;
//import com.ta.sia.entity.Penjualan;
//import com.ta.sia.entity.Persediaan;
//import com.ta.sia.entity.Rekening;
//import com.ta.sia.entity.Transaksi;
//import com.ta.sia.repository.AccountLinkRepository;
//import com.ta.sia.repository.JurnalRepository;
//import com.ta.sia.repository.PembelianRepository;
//import com.ta.sia.repository.PenjualanRepository;
//import com.ta.sia.repository.RekeningRepository;
//import com.ta.sia.repository.UserRepository;
//
//@Controller
//@RequestMapping("")
//public class LaporanController {
//
//	@Autowired
//	UserRepository ur;
//	@Autowired
//	RekeningRepository rr;
//	@Autowired
//	JurnalRepository jr;
//	@Autowired
//	PembelianRepository pr;
//	@Autowired
//	PenjualanRepository pnr;
//	@Autowired
//	AccountLinkRepository alr;
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
////	@GetMapping("")
////	public String index(Model model, Principal p) {
////		model.addAttribute("menu", "Dashboard");
////		model.addAttribute("submenu", "");
////		model.addAttribute("user", ur.findByUsername(p.getName()).get());
////		return "corona/index";
////	}
//	
//	@GetMapping("/neraca")
//	public String neraca(Model model, Principal p) {
//		model.addAttribute("menu", "Laporan");
//		model.addAttribute("submenu", "Neraca");
//		model.addAttribute("user", ur.findByUsername(p.getName()).get());
//		model.addAttribute("periode", new Date(System.currentTimeMillis()));
//		Rekening.setPeriode(new Date(System.currentTimeMillis()));
//		Persediaan.setPeriode(new Date(System.currentTimeMillis()));
//		Rekening.setAkhirPeriode(12);
//		setTransaksi();
//		AccountLink al = alr.findById(Long.valueOf(1)).get();
//		Rekening labaRugi = rr.findById(Long.valueOf(3)).get();
//		al.getRekLabaDitahan().setSaldo(labaRugi.getSaldo() - labaRugi.getSaldoPeriodik());
//		
//		model.addAttribute("neraca", rr.findById(Long.valueOf(2)).get());
//		return "corona/neraca";
//	}
//	
//	@GetMapping("/neraca/{periode}")
//	public String neraca(Model model, Principal p, @PathVariable Date periode) {
//		model.addAttribute("menu", "Laporan");
//		model.addAttribute("submenu", "Neraca");
//		model.addAttribute("user", ur.findByUsername(p.getName()).get());
//		model.addAttribute("periode", periode);
//		Rekening.setPeriode(periode);
//		Persediaan.setPeriode(periode);
//		setTransaksi();
//		AccountLink al = alr.findById(Long.valueOf(1)).get();
//		Rekening labaRugi = rr.findById(Long.valueOf(3)).get();
//		al.getRekLabaDitahan().setSaldo(labaRugi.getSaldo() - labaRugi.getSaldoPeriodik());
//		al.getRekLabaTahunBerjalan().setSaldo(labaRugi.getSaldoPeriodik());
//		model.addAttribute("neraca", rr.findById(Long.valueOf(2)).get());
//		return "corona/neraca";
//	}
//	
//	@GetMapping("/labarugi")
//	public String labarugi(Model model, Principal p) {
//		model.addAttribute("menu", "Laporan");
//		model.addAttribute("submenu", "Laba Rugi");
//		model.addAttribute("user", ur.findByUsername(p.getName()).get());
//		model.addAttribute("periode", new Date(System.currentTimeMillis()));
//		Rekening.setPeriode(new Date(System.currentTimeMillis()));
//		Persediaan.setPeriode(new Date(System.currentTimeMillis()));
//		Rekening.setAkhirPeriode(12);
//		setTransaksi();
//		Rekening labarugi = rr.findById(Long.valueOf(3)).get();
//		Rekening tam = new Rekening();
//		tam.getChild().add(labarugi);
//		model.addAttribute("labarugi", tam);
//		return "corona/labarugi";
//	}
//	
//	@GetMapping("/labarugi/{periode}")
//	public String labarugi(Model model, Principal p, @PathVariable Date periode) {
//		model.addAttribute("menu", "Laporan");
//		model.addAttribute("submenu", "Laba Rugi");
//		model.addAttribute("user", ur.findByUsername(p.getName()).get());
//		model.addAttribute("periode", periode);
//		Rekening.setPeriode(periode);
//		Persediaan.setPeriode(periode);
//		Rekening.setAkhirPeriode(12);
//		setTransaksi();
//		Rekening labarugi = rr.findById(Long.valueOf(3)).get();
//		Rekening tam = new Rekening();
//		tam.getChild().add(labarugi);
//		model.addAttribute("labarugi", tam);
//		System.out.println(Rekening.getPeriode() + " : " + Rekening.getAkhirPeriode());
//		return "corona/labarugi";
//	}
//	
//	@GetMapping("/ekuitas")
//	public String ekuitas(Model model, Principal p) {
//		model.addAttribute("menu", "Laporan");
//		model.addAttribute("submenu", "Laporan Ekuitas");
//		model.addAttribute("user", ur.findByUsername(p.getName()).get());
//		model.addAttribute("periode", new Date(System.currentTimeMillis()));
//		Rekening.setPeriode(new Date(System.currentTimeMillis()));
//		Persediaan.setPeriode(new Date(System.currentTimeMillis()));
//		setTransaksi();
//		Rekening labarugi = rr.findById(Long.valueOf(3)).get();
//		Rekening tam = new Rekening();
//		tam.getChild().add(labarugi);
//		model.addAttribute("labarugi", tam);
//		return "corona/labarugi";
//	}
//	
//	@GetMapping("/ekuitas/{periode}")
//	public String ekuitas(Model model, Principal p, @PathVariable Date periode) {
//		model.addAttribute("menu", "Laporan");
//		model.addAttribute("submenu", "Laba Rugi");
//		model.addAttribute("user", ur.findByUsername(p.getName()).get());
//		model.addAttribute("periode", periode);
//		Rekening.setPeriode(periode);
//		Persediaan.setPeriode(periode);
//		Rekening labarugi = rr.findById(Long.valueOf(3)).get();
//		Rekening tam = new Rekening();
//		tam.getChild().add(labarugi);
//		model.addAttribute("labarugi", tam);
//		return "corona/labarugi";
//	}
//}
