package com.ta.sia.restController;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ta.sia.repository.AccountLinkRepository;
import com.ta.sia.repository.JurnalRepository;
import com.ta.sia.repository.PembelianRepository;
import com.ta.sia.repository.PenjualanRepository;
import com.ta.sia.repository.RekeningRepository;
import com.ta.sia.repository.UserRepository;
import com.ta.sia.service.ExportToExcelService;
import com.ta.sia.service.LaporanService;
import com.ta.sia.entity.AccountLink;
import com.ta.sia.entity.Jurnal;
import com.ta.sia.entity.Pembelian;
import com.ta.sia.entity.Penjualan;
import com.ta.sia.entity.Persediaan;
import com.ta.sia.entity.Rekening;
import com.ta.sia.entity.Transaksi;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.swing.JRadioButton;

//import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/api/laporan")
public class LaporanRestController {

	@Autowired
	LaporanService ls;
	
	@Autowired
	JurnalRepository jr;
	
	@Autowired
	ExportToExcelService es;
	
	@GetMapping("/neraca")
	public ResponseEntity<Object> neraca() {
		return this.neraca(new Date(System.currentTimeMillis()));
	}
	
	@GetMapping("/neraca/{periode}")
	public ResponseEntity<Object> neraca(@PathVariable Date periode) {
		Map<String,Object> obj = new HashMap<String, Object>();
		Map<String, Object> obj2 = new LinkedHashMap<String, Object>();
		obj2.put("periode", periode);
		obj2.put("laporan", ls.neraca(periode));
		obj.put("status", true);
		obj.put("messages", "Data berhasil diambil");
		obj.put("data", obj2);
		return new ResponseEntity<>(obj,HttpStatus.OK);
	}
	
	@GetMapping("/laba-rugi")
	public ResponseEntity<Object> labarugi() {
		return this.labarugi(new Date(System.currentTimeMillis()));
	}
	
	@GetMapping("/laba-rugi/yearly/{startPeriode}/{endPeriode}")
	public ResponseEntity<Object> labarugi(@PathVariable Integer startPeriode,@PathVariable Integer endPeriode) {
		Map<String,Object> obj = new HashMap<String, Object>();
		List<Map<String, Object>> obj2 = new ArrayList<>();
		
		for(Integer periode = startPeriode; periode <= endPeriode; periode += 1) {
			
			Map<String, Object> labarugi = new LinkedHashMap<>();
			
			Calendar cal = Calendar.getInstance();
			cal.set(periode, 11, 31);
			
			Rekening laporan = ls.labarugi(new Date(cal.getTimeInMillis()));
			labarugi.put("laporan", laporan);
			obj2.add(labarugi);
		}
	
		obj.put("status", true);
		obj.put("messages", "Data berhasil diambil");
		obj.put("data", obj2);
		return new ResponseEntity<>(obj,HttpStatus.OK);
	}
	
	@GetMapping("/laba-rugi/{periode}")
	public ResponseEntity<Object> labarugi(@PathVariable Date periode) {
		Map<String,Object> obj = new HashMap<String, Object>();
		Map<String, Object> obj2 = new LinkedHashMap<String, Object>();
		obj2.put("periode", periode);
		obj2.put("laporan", ls.labarugi(periode)); // Need to fix
		obj.put("status", true);
		obj.put("messages", "Data berhasil diambil");
		obj.put("data", obj2);
		return new ResponseEntity<>(obj,HttpStatus.OK);
	}
	
	@GetMapping("/export")
	public ResponseEntity<Object> export() {
		return this.export(new Date(System.currentTimeMillis()));
	}
	
	@GetMapping("/export/{periode}")
	public ResponseEntity<Object> export(@PathVariable Date periode) {
		Map<String,Object> obj = new HashMap<String, Object>();
		obj.put("status", es.export(periode));
		obj.put("messages", "Data berhasil diexport");
		return new ResponseEntity<>(obj,HttpStatus.OK);
	}
	
}