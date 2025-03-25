//package com.ta.sia.restController;
//
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.beans.factory.annotation.Autowired;
//import com.ta.sia.repository.PembelianDetailsRepository;
//import com.ta.sia.repository.RekeningRepository;
//import com.ta.sia.repository.AccountLinkRepository;
//import com.ta.sia.entity.PembelianDetails;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
////import javax.validation.Valid;
//import org.springframework.validation.BindingResult;
//import org.springframework.validation.annotation.Validated;
//
//@RestController
//@RequestMapping("/api/pembelian_details")
//public class PembelianDetailsRestController {
//
//	@Autowired
//	PembelianDetailsRepository pdr;
//	@Autowired
//	RekeningRepository rr;
//	@Autowired
//	AccountLinkRepository alr;
//
//	@GetMapping("")
//	public ResponseEntity<Object> index(){
//		List<PembelianDetails> data = pdr.findAll();
//		Map<String,Object> obj = new HashMap<String, Object>();
//		obj.put("status", true);
//		obj.put("messages", "Data berhasil diambil");
//		obj.put("data", data);
//		return new ResponseEntity<>(obj,HttpStatus.OK);
//	}
//
//	@GetMapping("/{id}")
//	public ResponseEntity<Object> details(@PathVariable Long id){
//		PembelianDetails data = pdr.findById(id).get();
//		Map<String,Object> obj = new HashMap<String, Object>();
//		obj.put("status", true);
//		obj.put("messages", "Data berhasil diambil");
//		obj.put("data", data);
//		return new ResponseEntity<>(obj,HttpStatus.OK);
//	}
//
//	@PostMapping("")
//	public ResponseEntity<Object> save(@Validated PembelianDetails pembelianDetails, BindingResult bindingResult){
//		Map<String,Object> obj = new HashMap<String, Object>();
//		if (bindingResult.hasErrors()) {
//			List<String> errors = bindingResult.getAllErrors().stream()
//			.map(e -> e.getDefaultMessage())
//			.collect(Collectors.toList());
//			obj.put("status", false);
//			obj.put("messages", errors);
//		}else{
//			obj.put("status", true);
//			obj.put("messages", "Data berhasil disimpan");
//			pdr.save(pembelianDetails);
////			Rekening rekPembelian = al.getRekPembelian();
////			Rekening rekPelunasanUtang = al.getRekPelunasanUtang();
////			Rekening rekeningBrg = pembelianDetails.getBarang().getRekInventory();
////			Long hasil = Long.valueOf("0");
////			hasil = pembelianDetails.getHarga() * pembelianDetails.getJumlah();
////			rekPembelian.setSaldo(rekPembelian.getSaldo() + hasil);
////			rekeningBrg.setSaldo(rekeningBrg.getSaldo() + hasil);
////			rr.save(rekPembelian);
////			rr.save(rekeningBrg);
//		}
//		return new ResponseEntity<>(obj,HttpStatus.OK);
//	}
//
//	@GetMapping("/delete/{id}")
//	public ResponseEntity<Object> delete(@PathVariable Long id){
//		pdr.deleteById(id);
//		Map<String,Object> obj = new HashMap<String, Object>();
//		obj.put("status", true);
//		obj.put("messages", "Data berhasil dihapus");
//		return new ResponseEntity<>(obj,HttpStatus.OK);
//	}
//
//}
//
//
