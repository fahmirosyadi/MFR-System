package com.ta.sia.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;

public class AccessDeniedHandler implements org.springframework.security.web.access.AccessDeniedHandler{

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		Map<String,Object> obj = new HashMap<String, Object>();
		obj.put("status", false);
		obj.put("messages", "Akses dilarang");
	}
	
	

}
