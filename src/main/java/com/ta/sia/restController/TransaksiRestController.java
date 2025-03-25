package com.ta.sia.restController;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ta.sia.entity.DatatablesResponse;
import com.ta.sia.entity.Transaksi;
import com.ta.sia.repository.AccountLinkRepository;

@RestController
@CrossOrigin
public abstract class TransaksiRestController <T extends Transaksi>  extends AbstractRestController<T>{
	
	@Autowired
	protected AccountLinkRepository alr;
	
	//@Override
	@GetMapping("")
	public ResponseEntity<Object> index(Principal p){
		return this.indexResponse(this.getRepository().findAll(Sort.by("tanggal")));
	}
	
	@PostMapping("datatables")
	@Override
	public HashMap<String, Object> datatables(DatatablesResponse dr){
		HashMap<String, Object> obj = new HashMap<String, Object>();
		List<T> result = this.getRepository().findAll(Sort.by("tanggal"));
		obj.put("draw", dr.draw);
		obj.put("recordsTotal",result.size());
		obj.put("recordsFiltered",result.size());
		obj.put("data", result);
		return obj;
	}
	
}
