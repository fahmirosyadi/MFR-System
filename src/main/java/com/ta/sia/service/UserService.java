package com.ta.sia.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ta.sia.entity.User;
import com.ta.sia.repository.UserRepository;

@Service
@Transactional
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository ur;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = ur.findByUsername(username);
		if (user != null && user.isPresent()) {
			user.get().getAuthorities();
			return user.get();	
		}else {
			throw new UsernameNotFoundException("User dengan nama " + username + " tidak ditemukan");
		}
		
	}
}
