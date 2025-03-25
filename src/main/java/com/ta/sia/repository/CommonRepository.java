package com.ta.sia.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CommonRepository<T, A> extends JpaRepository<T, A> {
	
	public String query = "";
	public Page<T> findLike(String value, Pageable pageable);
	
}
