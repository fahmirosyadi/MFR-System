package com.ta.sia.restController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ta.sia.entity.AccountLink;
import com.ta.sia.entity.Jurnal;
import com.ta.sia.entity.PembayaranUtang;
import com.ta.sia.entity.PembayaranUtangDetails;
import com.ta.sia.repository.CommonRepository;
import com.ta.sia.repository.PembayaranUtangDetailsRepository;
import com.ta.sia.repository.PembayaranUtangRepository;

@RestController
@CrossOrigin
@RequestMapping(value = "api/pembayaran-utang")
public class PembayaranUtangRestController extends TransaksiRestController<PembayaranUtang> {

	@Autowired
	PembayaranUtangRepository pur;
	@Autowired
	PembayaranUtangDetailsRepository pudr;
	
	@Override
	@GetMapping("")
	public ResponseEntity<Object> index(Principal p){
		List<Jurnal> lJurnal = new ArrayList<Jurnal>();
//		for(PembayaranUtang pu : pur.findAll(Sort.by("tanggal"))) {
//			Jurnal j = pu.getJurnal();
//			lJurnal.add(j);
//		}
		Map<String,Object> obj = new HashMap<String, Object>();
		obj.put("status", true);
		obj.put("messages", "Data berhasil diambil");
		obj.put("data", lJurnal);
		return new ResponseEntity<>(obj,HttpStatus.OK);
	}
	
	@PostMapping("")
	public ResponseEntity<Object> save(@RequestBody @Validated PembayaranUtang pelunasanUtang, BindingResult bindingResult){
		return this.save(pelunasanUtang, bindingResult, (Map<String, Object> obj) -> {
			if(pelunasanUtang.getId() != null) {
				PembayaranUtang pu = pur.findById(pelunasanUtang.getId()).get();
				pudr.deleteInBatch(pu.getPembayaranUtangDetails());
				for(PembayaranUtangDetails pud : pelunasanUtang.getPembayaranUtangDetails()) {
					pud.setId(null);
				}
			}
			pur.save(pelunasanUtang);
		});
	}

	@Override
	public CommonRepository<PembayaranUtang, Long> getRepository() {
		return this.pur;
	}

	@Override
	public List<String> getDatatablesFields() {
		return this.getDatatablesFields(PembayaranUtang.class.getDeclaredFields());
	}
	
}
