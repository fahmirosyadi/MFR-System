package com.ta.sia.restController;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.ta.sia.repository.CommonRepository;
import com.ta.sia.repository.CustomerRepository;
import com.ta.sia.entity.Customer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RequestMapping("/api/customer")
public class CustomerRestController extends AbstractRestController<Customer> {

	@Autowired
	CustomerRepository cr;

	@GetMapping("/{customer}/piutang")
	public ResponseEntity<Object> piutang(@PathVariable Customer customer){
		Map<String,Object> obj = new HashMap<String, Object>();
		Map<String, Object> obj2 = new LinkedHashMap<String, Object>();
		obj2.put("id", customer.getId());
		obj2.put("nama", customer.getNama());
		obj2.put("alamat", customer.getAlamat());
		obj2.put("telp", customer.getTelp());
		obj2.put("bukuPembantuPiutang", customer.getBukuPembantuPiutang());
		obj.put("status", true);
		obj.put("messages", "Data berhasil diambil");
		obj.put("data", obj2);
		return new ResponseEntity<>(obj,HttpStatus.OK);
	}

	@Override
	public CommonRepository<Customer, Long> getRepository() {
		return this.cr;
	}

	@Override
	public List<String> getDatatablesFields() {
		return this.getDatatablesFields(Customer.class.getDeclaredFields());
	}

}