//package com.ta.sia.restController;
//
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.beans.factory.annotation.Autowired;
//import com.ta.sia.repository.JurnalDetailsRepository;
//import com.ta.sia.repository.RekeningRepository;
//import com.ta.sia.entity.JurnalDetails;
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
//@RequestMapping("/api/jurnal_details")
//public class JurnalDetailsRestController {
//
//	@Autowired
//	JurnalDetailsRepository jdr;
//	@Autowired
//	RekeningRepository rr;
//
//	@GetMapping("")
//	public ResponseEntity<Object> index(){
//		List<JurnalDetails> data = jdr.findAll();
//		Map<String,Object> obj = new HashMap<String, Object>();
//		obj.put("status", true);
//		obj.put("messages", "Data berhasil diambil");
//		obj.put("data", data);
//		return new ResponseEntity<>(obj,HttpStatus.OK);
//	}
//
//	@GetMapping("/{id}")
//	public ResponseEntity<Object> details(@PathVariable Long id){
//		JurnalDetails data = jdr.findById(id).get();
//		Map<String,Object> obj = new HashMap<String, Object>();
//		obj.put("status", true);
//		obj.put("messages", "Data berhasil diambil");
//		obj.put("data", data);
//		return new ResponseEntity<>(obj,HttpStatus.OK);
//	}
//
//	@PostMapping("")
//	public ResponseEntity<Object> save(@Validated JurnalDetails jurnalDetails, BindingResult bindingResult){
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
//			if (jurnalDetails.getDebet() == null) {
//				jurnalDetails.setDebet(Long.valueOf("0"));
//			}
//			if (jurnalDetails.getKredit() == null) {
//				jurnalDetails.setKredit(Long.valueOf("0"));
//			}
//			jdr.save(jurnalDetails);
////			Rekening rekening = jurnalDetails.getRekening();
////			Long hasil = Long.valueOf("0");
////			if (rekening.getJenis().equals("assets")) {
////				hasil = jurnalDetails.getDebet() - jurnalDetails.getKredit();
////			}else{
////				hasil = jurnalDetails.getKredit() - jurnalDetails.getDebet();
////			}
////			rekening.setSaldo(rekening.getSaldo() + hasil);
////			System.out.println(rekening.getSaldo());
////			rr.save(rekening);
//		}
//		return new ResponseEntity<>(obj,HttpStatus.OK);
//	}
//
//	@GetMapping("/delete/{id}")
//	public ResponseEntity<Object> delete(@PathVariable Long id){
//		jdr.deleteById(id);
//		Map<String,Object> obj = new HashMap<String, Object>();
//		obj.put("status", true);
//		obj.put("messages", "Data berhasil dihapus");
//		return new ResponseEntity<>(obj,HttpStatus.OK);
//	}
//
//}
//
//
