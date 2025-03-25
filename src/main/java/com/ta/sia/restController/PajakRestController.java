package com.ta.sia.restController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ta.sia.entity.Pajak;
import com.ta.sia.repository.CommonRepository;
import com.ta.sia.repository.PajakRepository;

@RestController
@CrossOrigin
@RequestMapping("/api/pajak")
public class PajakRestController extends AbstractRestController<Pajak> {

	@Autowired
	PajakRepository pr;
	
	@Override
	public List<String> getDatatablesFields() {
		return this.getDatatablesFields(Pajak.class.getDeclaredFields());
	}

	@Override
	public CommonRepository<Pajak, Long> getRepository() {
		// TODO Auto-generated method stub
		return this.pr;
	}
}
