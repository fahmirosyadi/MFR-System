package com.ta.sia.restController;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.ta.sia.repository.CommonRepository;
import com.ta.sia.repository.SupplierRepository;
import com.fasterxml.jackson.annotation.JsonView;
import com.ta.sia.config.View.Detail;
import com.ta.sia.entity.Supplier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/supplier")
public class SupplierRestController extends AbstractRestController<Supplier>  {

	@Autowired
	SupplierRepository sr;
	
	@GetMapping("/select")
	public HashMap<String, Object> select(@RequestParam(defaultValue = "",name = "term") String search){ 
		Pageable pageable = PageRequest.of(0, 5);
		HashMap<String, Object> hasil = new HashMap<String, Object>();
		List<HashMap<String, Object>> result = new ArrayList<HashMap<String,Object>>();
		Map<String, Object> pagination = new HashMap<String, Object>();
		pagination.put("more", true);
		for(Supplier sup : sr.findLike(search, pageable).getContent()) {
			HashMap<String, Object> obj = new HashMap<String, Object>();
			obj.put("id", sup.getId());
			obj.put("text", sup.getNama());
			result.add(obj);
		}
		hasil.put("results", result);
		hasil.put("pagination", pagination);
		return hasil;
	}
	
	@GetMapping("/{supplier}/utang")
	public ResponseEntity<Object> utang(@PathVariable Supplier supplier){
		Map<String,Object> obj = new HashMap<String, Object>();
		Map<String, Object> obj2 = new LinkedHashMap<String, Object>();
		obj2.put("id", supplier.getId());
		obj2.put("nama", supplier.getNama());
		obj2.put("alamat", supplier.getAlamat());
		obj2.put("telp", supplier.getTelp());
		obj2.put("bukuPembantuUtang", supplier.getBukuPembantuUtang());
		obj.put("status", true);
		obj.put("messages", "Data berhasil diambil");
		obj.put("data", obj2);
		return new ResponseEntity<>(obj,HttpStatus.OK);
	}

	@Override
	public CommonRepository<Supplier, Long> getRepository() {
		return this.sr;
	}
	
	@Override
	public List<String> getDatatablesFields() {
		return this.getDatatablesFields(Supplier.class.getDeclaredFields());
	}

}