package com.mfr.system.restController;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mfr.system.entity.FileUpload;
import com.mfr.system.service.FileUploadService;

@RestController
@RequestMapping("/api/file")
public class FileUploadRestController {
	
	@Autowired
	FileUploadService fus;

	@PostMapping("/upload")
	public ResponseEntity<Object> upload(FileUpload data){
		String fileName = this.fus.upload(data.getFile(), data.getOldName(), data.isHapus(), data.getFolder());
		Map<String,Object> obj = new HashMap<String, Object>();
		obj.put("status", true);
		obj.put("data", fileName);
		return new ResponseEntity<>(obj,HttpStatus.OK);
		
	}
	
}
