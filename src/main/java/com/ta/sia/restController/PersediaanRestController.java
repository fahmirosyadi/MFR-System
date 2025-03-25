package com.ta.sia.restController;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;

import com.ta.sia.repository.CommonRepository;
import com.ta.sia.repository.PersediaanRepository;
import com.ta.sia.entity.Persediaan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/persediaan")
public class PersediaanRestController extends AbstractRestController<Persediaan> {
	
	@Autowired
	PersediaanRepository pr;
	
	@GetMapping("/{p}/stok")
	public ResponseEntity<Object> stok(@PathVariable Persediaan p){
		Map<String,Object> obj = new HashMap<String, Object>();
		Map<String, Object> obj2 = new LinkedHashMap<String, Object>();
		obj2.put("id", p.getId());
		obj2.put("barang", p.getBarang());
		obj2.put("stokAkhir", p.getStokAkhir());
		obj2.put("stok", p.getStok());
		obj.put("status", true);
		obj.put("messages", "Data berhasil diambil");
		obj.put("data", obj2);
		return new ResponseEntity<>(obj,HttpStatus.OK);
	}

	@Override
	public CommonRepository<Persediaan, Long> getRepository() {
		return this.pr;
	}
	
	@Override
	public List<String> getDatatablesFields() {
		return this.getDatatablesFields(Persediaan.class.getDeclaredFields());
	}

}