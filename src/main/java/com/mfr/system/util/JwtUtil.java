package com.mfr.system.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.mfr.system.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

	private final String SECRET_KEY = "secret";
	
	public String extractUsername(String token) {
		return extractClaim(token,Claims::getSubject);
	}
	
	public<T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaimsToken(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaimsToken(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	}

	public String generateToken(User user) {
		Map<String, Object> claims = new HashMap<String, Object>();
		return createToken(claims, user.getUsername());
	}

	private String createToken(Map<String, Object> claims, String subject) {
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
//		.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
		.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
		.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
	}
	
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = this.extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	private boolean isTokenExpired(String token) {
		return exctractExpiration(token).before(new Date());
	}

	private Date exctractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
	
}
