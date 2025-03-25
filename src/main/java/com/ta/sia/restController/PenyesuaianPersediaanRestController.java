package com.ta.sia.restController;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;

import com.ta.sia.repository.PenyesuaianPersediaanDetailsRepository;
import com.ta.sia.repository.PenyesuaianPersediaanRepository;
import com.ta.sia.repository.CommonRepository;
import com.ta.sia.entity.PenyesuaianPersediaan;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

@RestController
@CrossOrigin
@RequestMapping("/api/penyesuaian-persediaan")
public class PenyesuaianPersediaanRestController extends TransaksiRestController<PenyesuaianPersediaan> {

	@Autowired
	PenyesuaianPersediaanRepository  bkr;
	@Autowired
	PenyesuaianPersediaanDetailsRepository bkdr;

	@PostMapping("")
	@Transactional
	public ResponseEntity<Object> save(@RequestBody @Validated PenyesuaianPersediaan data, BindingResult bindingResult){
		return this.save(data, bindingResult, (Map<String, Object> obj) -> {
			data.detailsFilter();
			data.setJenis("penyesuaian-persediaan");
			if(data.getId() != null) {
				PenyesuaianPersediaan bk = bkr.findById(data.getId()).get();
				bkdr.deleteInBatch(bk.getPenyesuaianPersediaanDetails());
			}
			bkr.save(data);
		});
	}

	@Override
	public CommonRepository<PenyesuaianPersediaan, Long> getRepository() {
		return this.bkr;
	}

	@Override
	public List<String> getDatatablesFields() {
		return this.getDatatablesFields(PenyesuaianPersediaan.class.getDeclaredFields());
	}

}