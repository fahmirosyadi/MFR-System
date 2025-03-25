package com.ta.sia.restController;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import com.ta.sia.repository.JurnalRepository;
import com.ta.sia.repository.JurnalViewRepository;
import com.ta.sia.repository.CommonRepository;
import com.ta.sia.repository.JurnalDetailsRepository;
import com.ta.sia.dto.ResponseDto;
import com.ta.sia.entity.DatatablesResponse;
import com.ta.sia.entity.Jurnal;
import com.ta.sia.entity.JurnalDetails;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

@RestController
@CrossOrigin
@RequestMapping("/api/jurnal")
public class JurnalRestController extends TransaksiRestController<Jurnal> {

	@Autowired
	JurnalDetailsRepository jdr;
	@Autowired
	JurnalRepository jr;
	@Autowired
	JurnalViewRepository jvr;
	
	@Override
	@GetMapping("")
	public ResponseEntity<Object> index(Principal p){
		return new ResponseEntity<>(new ResponseDto(true, "Data berhasil diambil", jr.findByJenis("jurnal",Sort.by("tanggal"))),HttpStatus.OK);
	}
	
	@Override
	@PostMapping("/datatables")
	public HashMap<String, Object> datatables(DatatablesResponse dr) {
		HashMap<String, Object> obj = new HashMap<String, Object>();
		obj.put("draw", dr.draw);
		obj.put("recordsTotal",jr.count());
		obj.put("recordsFiltered",jr.count());
		obj.put("data", jr.findByJenis("jurnal",Sort.by("tanggal")));
		return obj;
	}
	
	@GetMapping("/view")
	public ResponseEntity<Object> view(){
		return new ResponseEntity<>(new ResponseDto(true, "Data berhasil diambil", jvr.findAll()),HttpStatus.OK);
	}


	@GetMapping("/last")
	public ResponseEntity<Object> last(){
		return new ResponseEntity<>(new ResponseDto(true, "Data berhasil diambil", jr.findLast()),HttpStatus.OK);
	}

	@PostMapping("")
	public ResponseEntity<Object> save(@RequestBody @Validated Jurnal jurnal, BindingResult bindingResult){
		return this.save(jurnal, bindingResult, (Map<String, Object> obj) -> {
			if(jurnal.getId() != null) {
				Jurnal j = jr.findById(jurnal.getId()).get();
				jdr.deleteInBatch(j.getJurnalDetails());
				for(JurnalDetails jd : jurnal.getJurnalDetails()) {
					jd.setId(null);
				}
			}
			jr.save(jurnal);
		});
	}
	
	@GetMapping("/jurnalUmum")
	public List<JurnalDetails> getJurnalUmum() {
		List<JurnalDetails> result = new ArrayList<JurnalDetails>();
		for(JurnalDetails jd : jdr.findAll()) {
			result.add(jd);
		}
		return result;
	}

	@Override
	public CommonRepository<Jurnal, Long> getRepository() {
		return this.jr;
	}

	@Override
	public List<String> getDatatablesFields() {
		return this.getDatatablesFields(Jurnal.class.getDeclaredFields());
	}

}