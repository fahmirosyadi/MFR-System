package com.ta.sia.restController;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;

import com.ta.sia.repository.CommonRepository;
import com.ta.sia.repository.PenjualanDetailsRepository;
import com.ta.sia.repository.PenjualanRepository;
import com.ta.sia.entity.AccountLink;
import com.ta.sia.entity.Penjualan;
import com.ta.sia.entity.PenjualanDetails;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

@RestController
@CrossOrigin
@RequestMapping("/api/penjualan")
public class PenjualanRestController extends TransaksiRestController<Penjualan> {

	@Autowired
	PenjualanRepository pr;
	@Autowired
	PenjualanDetailsRepository pdr;

	@PostMapping("")
	public ResponseEntity<Object> save(@RequestBody @Validated Penjualan penjualan, BindingResult bindingResult){
		return this.save(penjualan, bindingResult, (Map<String, Object> obj) -> {
			if(penjualan.getId() != null) {
				Optional<Penjualan> p = pr.findById(penjualan.getId());
				pdr.deleteInBatch(p.get().getPenjualanDetails());
				for(PenjualanDetails pd : penjualan.getPenjualanDetails()) {
					pd.setId(null);
				}
			}
			pr.save(penjualan);
		});
	}

	@Override
	public CommonRepository<Penjualan, Long> getRepository() {
		return this.pr;
	}

	@Override
	public List<String> getDatatablesFields() {
		return this.getDatatablesFields(Penjualan.class.getDeclaredFields());
	}

}