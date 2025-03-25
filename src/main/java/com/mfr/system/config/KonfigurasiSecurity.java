package com.mfr.system.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@EnableWebSecurity
@Configuration
public class KonfigurasiSecurity extends WebSecurityConfigurerAdapter {

	@Autowired
	UserDetailsService userDetailsService;
	@Autowired
	JwtFilter jwtFilter;
	
	

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(this.encodePwd());
	}
	
	@Bean
	public BCryptPasswordEncoder encodePwd() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().authorizeRequests()
			
			.antMatchers("/","/api/user/login","/api/user/count","/api/user/register","/api/user/resetPassword","/api/role/**","/api/user/**","/api/rekening/**","/api/supplier/**","/api/customer/**").permitAll()
//			.antMatchers("/user","/api/user","/api/user/{id}").hasAnyRole("ADMIN")
			.antMatchers("/api/menu/**").authenticated()
//			.antMatchers("").hasAnyRole("ADMIN")
////			.antMatchers("/api/persediaan/**").hasAnyRole("INVENTORY","ADMIN")
//			.anyRequest().authenticated()
			.and().formLogin().loginPage("/login").failureUrl("/login-error")
			.and().addFilterAfter(new CsrfToCookieFilter(), CsrfFilter.class).csrf().csrfTokenRepository(csrfTokenRepository())
			.and().csrf().ignoringAntMatchers("/api/user/login")
			.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and().addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
			.csrf().disable();
		
		http
	    .exceptionHandling()
//	    .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
	    .authenticationEntryPoint(new Http403ForbiddenEntryPoint());
		
	}
	
	public CsrfTokenRepository csrfTokenRepository() {
		HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		repository.setHeaderName("X-XSRF-TOKEN");
		return repository;
	}
	
	

}