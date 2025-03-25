//package com.ta.sia.restController;
//
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.ta.sia.repository.PersediaanRepository;
//import com.ta.sia.repository.StokRepository;
//import com.ta.sia.entity.Stok;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Optional;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
////import javax.validation.Valid;
//import org.springframework.validation.BindingResult;
//import org.springframework.validation.annotation.Validated;
//
//import com.ta.sia.entity.Persediaan;
//
//@RestController
//@RequestMapping("/api/stok")
//public class StokRestController {
//
//	@Autowired
//	StokRepository sr;
//	
//	@Autowired
//	PersediaanRepository pr;
//
//	@GetMapping("/persediaan/{id}/{type}")
//	public ResponseEntity<Object> index(@PathVariable Long id,@PathVariable String type){
//		Persediaan persediaan = pr.findById(id).get();
//		Map<String,Object> obj = new HashMap<String, Object>();
//		obj.put("status", true);
//		obj.put("messages", "Data berhasil diambil");
//		obj.put("data", new Persediaan(persediaan).generateStok(type));
//		return new ResponseEntity<>(obj,HttpStatus.OK);
//	}
//
//	@GetMapping("/{id}")
//	public ResponseEntity<Object> details(@PathVariable Long id){
//		Stok data = sr.findById(id).get();
//		Map<String,Object> obj = new HashMap<String, Object>();
//		obj.put("status", true);
//		obj.put("messages", "Data berhasil diambil");
//		obj.put("data", data);
//		return new ResponseEntity<>(obj,HttpStatus.OK);
//	}
//
//	@PostMapping("")
//	public ResponseEntity<Object> save(@Validated Stok stok, BindingResult bindingResult){
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
//			sr.save(stok);
//		}
//		return new ResponseEntity<>(obj,HttpStatus.OK);
//	}
//
//	@GetMapping("/delete/{id}")
//	public ResponseEntity<Object> delete(@PathVariable Long id){
//		sr.deleteById(id);
//		Map<String,Object> obj = new HashMap<String, Object>();
//		obj.put("status", true);
//		obj.put("messages", "Data berhasil dihapus");
//		return new ResponseEntity<>(obj,HttpStatus.OK);
//	}
//
//}