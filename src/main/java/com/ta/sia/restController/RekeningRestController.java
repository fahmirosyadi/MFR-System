package com.ta.sia.restController;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.apache.xmlbeans.impl.xb.xsdschema.All;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ta.sia.repository.AccountLinkRepository;
import com.ta.sia.repository.JurnalRepository;
import com.ta.sia.repository.PembelianRepository;
import com.ta.sia.repository.PenjualanRepository;
import com.ta.sia.repository.RekeningRepository;
import com.fasterxml.jackson.annotation.JsonView;
import com.ta.sia.config.View;
import com.ta.sia.entity.AccountLink;
import com.ta.sia.entity.Jurnal;
import com.ta.sia.entity.Pembelian;
import com.ta.sia.entity.Penjualan;
import com.ta.sia.entity.Persediaan;
import com.ta.sia.entity.Rekening;
import com.ta.sia.entity.Transaksi;
import com.ta.sia.entity.Tree;
import com.ta.sia.entity.TreeUtil;

import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

//import javax.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

@RestController
@CrossOrigin
@RequestMapping("/api/rekening")
public class RekeningRestController {

	@Autowired
	RekeningRepository rr;
	@Autowired
	AccountLinkRepository alr;
	@Autowired
	JurnalRepository jr;
	@Autowired
	PembelianRepository pr;
	@Autowired
	PenjualanRepository pnr;

	@GetMapping("")	
	public ResponseEntity<Object> index(){
		Map<String,Object> obj = new HashMap<String, Object>();
		obj.put("status", true);
		obj.put("messages", "Data berhasil diambil");
//		Persediaan.setPeriode(new Date(System.currentTimeMillis()));
		AccountLink al = alr.findAll().get(0);
		Rekening labaRugi = rr.findByJenis("labarugi").get(0);
		al.getRekLabaDitahan().setSaldo(labaRugi.getSaldo() - labaRugi.getSaldoPeriodik());
		al.getRekLabaTahunBerjalan().setSaldo(labaRugi.getSaldoPeriodik());
		obj.put("data", rr.findByJenis("rekening").get(0));
		
		return new ResponseEntity<>(obj,HttpStatus.OK);
	}
	@CrossOrigin
//	@JsonView(View.All.class)
	@PostMapping("/datatables")	
	public ResponseEntity<Object> toList(){
		Map<String,Object> obj = new HashMap<String, Object>();
		obj.put("status", true);
		obj.put("messages", "Data berhasil diambil");
//		Persediaan.setPeriode(new Date(System.currentTimeMillis()));
		Optional<AccountLink> al = alr.findById(Long.valueOf(1));
		List<Rekening> lr = rr.findByJenis("labarugi");
		Rekening labaRugi =  lr != null ? lr.get(0) : null;
		if(al.isPresent()) {
			al.get().getRekLabaDitahan().setSaldo(labaRugi.getSaldo() - labaRugi.getSaldoPeriodik());
			al.get().getRekLabaTahunBerjalan().setSaldo(labaRugi.getSaldoPeriodik());
		}
		List<Map<String, Object>> data = TreeUtil.toList(rr.findByJenis("rekening").get(0).getChild(),0);
		System.out.println("Size : " + data.size());
		obj.put("data", data);
		return new ResponseEntity<>(obj,HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Object> details(@PathVariable Long id){
		Map<String,Object> obj = new HashMap<String, Object>();
		obj.put("status", true);
		obj.put("messages", "Data berhasil diambil");
		obj.put("data", rr.findById(id));
		return new ResponseEntity<>(obj,HttpStatus.OK);
	}
	
	@GetMapping("/byParent/{parent}")
	public ResponseEntity<Object> findByParent(@PathVariable Long parent){
		Map<String,Object> obj = new HashMap<String, Object>();
		obj.put("status", true);
		obj.put("messages", "Data berhasil diambil");
		if(parent.equals(Long.valueOf(-1))) {
			obj.put("data", rr.findByParent(null));
		}
		return new ResponseEntity<>(obj,HttpStatus.OK);
	}
	
	@GetMapping("/findLeaf")
	@JsonView(View.All.class)
	public ResponseEntity<Object> findLeaf(){
		Map<String,Object> obj = new HashMap<String, Object>();
		obj.put("status", true);
		obj.put("messages", "Data berhasil diambil");
		obj.put("data", rr.findByChild(null));
		return new ResponseEntity<>(obj,HttpStatus.OK);
	}
	
	@GetMapping("/findLeafDetails")
	public ResponseEntity<Object> findLeafDetails(){
		Map<String,Object> obj = new HashMap<String, Object>();
		obj.put("status", true);
		obj.put("messages", "Data berhasil diambil");
		obj.put("data", rr.findByChild(null));
		return new ResponseEntity<>(obj,HttpStatus.OK);
	}

	@PostMapping("")
	public ResponseEntity<Object> save(@RequestBody @Validated Rekening rekening, BindingResult bindingResult){

		Map<String,Object> obj = new HashMap<String, Object>();
		if (bindingResult.hasErrors()) {
			List<String> errors = bindingResult.getAllErrors().stream()
			.map(e -> e.getDefaultMessage())
			.collect(Collectors.toList());
			obj.put("status", false);
			obj.put("messages", errors);
		}else{
			obj.put("status", true);
			obj.put("messages", "Data berhasil disimpan");
			rr.save(rekening);
		}
		return new ResponseEntity<>(obj,HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> delete(@PathVariable Long id){
		Map<String,Object> obj = new HashMap<String, Object>();
		Rekening rekening = rr.findById(id).get();
		if(rekening.isDefaultAccount()) {
			obj.put("status", true);
			obj.put("messages", "Rekening default tidak dapat dihapus!");	
		}else {
			System.out.println("Rekening " + rekening.getId());
			rr.deleteById(rekening.getId());
			obj.put("status", true);
			obj.put("messages", "Data berhasil dihapus");
		}
		return new ResponseEntity<>(obj,HttpStatus.OK);
	}
	
	@PostMapping("/saveAll")
	public ResponseEntity<Object> saveAll(@RequestBody List<Rekening> lRek) {
		Map<String,Object> obj = new HashMap<String, Object>();
		Double saldoDebet = Double.valueOf(0);
		Double saldoKredit = Double.valueOf(0);
		for(Rekening rek : lRek) {
			if(rek.getJenis().equals("assets") || rek.getJenis().equals("beban") || rek.getJenis().equals("hpp")) {
				saldoDebet += rek.getSaldoAwal();
			}else {
				saldoKredit += rek.getSaldoAwal();
			}
		}
		if(saldoDebet.equals(saldoKredit) == false) {
			obj.put("status", false);
			obj.put("messages", "Saldo awal tidak balance!");
		}else {
			rr.saveAll(lRek);
			obj.put("status", true);
			obj.put("messages", "Berhasil mengisi saldo awal!");
		}
		return new ResponseEntity<>(obj,HttpStatus.OK);
	}

}