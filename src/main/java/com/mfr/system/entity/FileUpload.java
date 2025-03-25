package com.mfr.system.entity;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @ToString
public class FileUpload {

	private MultipartFile file;
	private String oldName;
	private boolean hapus;
	private String folder;
	
}
