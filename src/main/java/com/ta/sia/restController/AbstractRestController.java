package com.ta.sia.restController;

import java.lang.reflect.Field;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.ta.sia.annotation.Orderable;
import com.ta.sia.config.View.All;
import com.ta.sia.config.View.Detail;
import com.ta.sia.dto.ResponseDto;
import com.ta.sia.entity.AbstractEntity;
import com.ta.sia.entity.DatatablesResponse;
import com.ta.sia.repository.CommonRepository;

@RestController
@CrossOrigin
public abstract class AbstractRestController <T extends AbstractEntity> {


	@PersistenceContext
	protected EntityManager em;
	
	Logger log = Logger.getLogger(this.getClass().getName());
	public abstract CommonRepository<T, Long> getRepository();
	
	
	public ResponseEntity<Object> indexResponse(List<? extends AbstractEntity> data, String message){
		return new ResponseEntity<>(new ResponseDto(true, message, data), HttpStatus.OK);
	}
	
	public ResponseEntity<Object> indexResponse(List<? extends AbstractEntity> data){
		return this.indexResponse(data, "Data berhasil diambil");
	}
	
	public ResponseEntity<Object> getResponse(AbstractEntity data, String message){
		return new ResponseEntity<>(new ResponseDto(true, "Data berhasil diambil", data),HttpStatus.OK);
	}
	
	public ResponseEntity<Object> getResponse(AbstractEntity data){
		return this.getResponse(data, "Data berhasil diambil");
	}
	
	@GetMapping("")
	public ResponseEntity<Object> index(Principal p){
		return this.indexResponse(this.getRepository().findAll());
	}
	
	@GetMapping("findAll")
	public ResponseEntity<Object> findAll(
			@RequestParam(name = "search", defaultValue = "") String search,
			@RequestParam(name = "lastPage", defaultValue = "") String lastPage
			){
		List<? extends AbstractEntity> data = this.getRepository().findAll();
		return this.indexResponse(data, "Data berhasil diambil");
	}
	
	@GetMapping("select2")
	public ResponseEntity<Object> select2(
			@RequestParam(name = "search", defaultValue = "") String search,
			@RequestParam(name = "lastPage", defaultValue = "") String lastPage
			){
		List<? extends AbstractEntity> data = this.getRepository().findAll();
		return this.indexResponse(data, "Data berhasil diambil");
	}
	
	public ResponseEntity<Object> save(@RequestBody @Valid T entity, BindingResult bindingResult, Consumer<Map<String, Object>> callback){
		this.log.info("Start Save");
		Map<String,Object> obj = new HashMap<String, Object>();
		if (bindingResult.hasErrors()) {
			List<String> errors = bindingResult.getAllErrors().stream()
			.map(e -> e.getDefaultMessage())
			.collect(Collectors.toList());
			return new ResponseEntity<>(new ResponseDto(false, errors.get(0)),HttpStatus.OK);
		}
		callback.accept(obj);
		if(obj.get("status") != null && !(boolean) obj.get("status")) {
			return new ResponseEntity<>(obj,HttpStatus.OK);
		}
		obj.put("status", true);
		obj.put("messages", "Data berhasil disimpan");
		return new ResponseEntity<>(obj,HttpStatus.OK);
	}
	
	@CrossOrigin
	@PostMapping("")
	public ResponseEntity<Object> save(@RequestBody @Valid T entity, BindingResult bindingResult){
		return this.save(entity, bindingResult, (Map<String, Object> obj) -> {
			this.getRepository().save(entity);
		});
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> delete(@PathVariable Long id){
		this.log.info("Start Delete");
		this.getRepository().deleteById(id);
		return new ResponseEntity<>(new ResponseDto(true, "Data berhasil dihapus", null),HttpStatus.OK);
	}
	

	@GetMapping("/{data}")
	public ResponseEntity<Object> details(@PathVariable T data){
		System.out.println(data);
		return this.getResponse(data);
	}
	
	public HashMap<String, Object> datatablesResponse(DatatablesResponse dr, Page<? extends AbstractEntity> result){
		HashMap<String, Object> obj = new HashMap<String, Object>();
		obj.put("draw", dr.draw);
		obj.put("recordsTotal",result.getTotalElements());
		obj.put("recordsFiltered",result.getTotalElements());
		obj.put("data", result.getContent());
		return obj;
	}
	
	public HashMap<String, Object> datatablesResponse(DatatablesResponse dr, List<? extends AbstractEntity> result){
		HashMap<String, Object> obj = new HashMap<String, Object>();
		obj.put("draw", dr.draw);
		obj.put("recordsTotal",result.size());
		obj.put("recordsFiltered",result.size());
		obj.put("data", result);
		return obj;
	}
	
	@PostMapping("/datatables")
	@JsonView(All.class)
	public HashMap<String, Object> datatables(@RequestBody DatatablesResponse dr){
		List<String> column = this.getDatatablesFields();
		Pageable pageable;
		if(dr.order[0].dir.equals("asc")) {
			pageable = PageRequest.of(dr.start / dr.length, dr.length, Sort.by(column.get(Integer.valueOf(dr.order[0].column) - 1)).ascending());
		}else {
			pageable = PageRequest.of(dr.start / dr.length, dr.length, Sort.by(column.get(Integer.valueOf(dr.order[0].column) - 1)).descending());
		}
		Page<? extends AbstractEntity> result = this.getRepository().findLike(dr.search.value, pageable);
		return this.datatablesResponse(dr, result);
	}
	
	public abstract List<String> getDatatablesFields();
	
	public List<String> getDatatablesFields(Field[] fields) {
		List<String> result = new ArrayList<>();
		for(Field f : fields) {
			if(f.isAnnotationPresent(Orderable.class)) {
				result.add(f.getName());
			}
		}
		return result;
	}
}
