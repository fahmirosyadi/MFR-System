package com.ta.sia.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter @AllArgsConstructor
public class ResponseDto {
	
	private Boolean status;
	private String messages;
	private Object data;
	
	public ResponseDto(Boolean status, String messages) {
		this.status = status;
		this.messages = messages;
	}
	
}
