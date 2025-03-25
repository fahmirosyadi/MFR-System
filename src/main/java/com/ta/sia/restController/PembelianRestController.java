package com.ta.sia.restController;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ta.sia.repository.PembelianRepository;
import com.ta.sia.repository.PembelianDetailsRepository;
import com.ta.sia.repository.CommonRepository;
import com.ta.sia.entity.AccountLink;
import com.ta.sia.entity.Pembelian;
import com.ta.sia.entity.PembelianDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

@RestController
@CrossOrigin
@RequestMapping("/api/pembelian")
public class PembelianRestController extends TransaksiRestController<Pembelian> {

	@Autowired
	PembelianRepository pr;
	@Autowired
	PembelianDetailsRepository pdr;

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@GetMapping("/last")
	public ResponseEntity<Object> last(){
		return this.getResponse(pr.findLast());
	}

	@PostMapping("")
	@Transactional
	public ResponseEntity<Object> save(@RequestBody @Validated Pembelian pembelian, BindingResult bindingResult){
		return this.save(pembelian, bindingResult, (Map<String, Object> obj) -> {
			if(pembelian.getId() != null) {
				Optional<Pembelian> p = pr.findById(pembelian.getId());
				if(p.isPresent()) {
					pdr.deleteInBatch(p.get().getPembelianDetails());
				}
				for(PembelianDetails pd : pembelian.getPembelianDetails()) {
					pd.setId(null);
				}
			}
			pr.save(pembelian);
		});
	}

	@Override
	public CommonRepository<Pembelian, Long> getRepository() {
		return this.pr;
	}

	@Override
	public List<String> getDatatablesFields() {
		return this.getDatatablesFields(Pembelian.class.getDeclaredFields());
	}

}