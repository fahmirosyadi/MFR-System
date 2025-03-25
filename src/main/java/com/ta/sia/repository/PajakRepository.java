package com.ta.sia.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ta.sia.entity.Pajak;

public interface PajakRepository extends CommonRepository<Pajak, Long>{

	@Query(value = "select * from pajak where nama like %:s%", nativeQuery = true)
	public Page<Pajak> findLike(@Param("s") String s, Pageable pageable);
	
}
