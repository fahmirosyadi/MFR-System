package com.ta.sia.config;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ta.sia.service.UserService;
import com.ta.sia.util.JwtUtil;

@Component
public class JwtFilter extends OncePerRequestFilter{

	Logger log = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	JwtUtil jwtUtil;
	@Autowired
	UserService us;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String jwt = request.getHeader("Authorization");
		this.log.info("JWT : " + jwt);
		String username = null;
		if(jwt != null && !jwt.equals("")) {
			try {
				username = jwtUtil.extractUsername(jwt);
				
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		
		if(username != null && !username.equals("")) {
			UserDetails userDetails = us.loadUserByUsername(username);
			if(jwtUtil.validateToken(jwt, userDetails)) {
				UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken
						(userDetails,null,userDetails.getAuthorities());
				upat.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(upat);
			
			}
		}else {
			this.log.info("Username : " + username);
		}
		filterChain.doFilter(request, response);
		
	}
}
