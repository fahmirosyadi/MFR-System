package com.ta.sia.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ta.sia.entity.Persediaan;


public interface PersediaanRepository extends CommonRepository<Persediaan, Long> {

	@Query(value = "select * from persediaan where barang like %:s%", nativeQuery = true)
	public Page<Persediaan> findLike(@Param("s") String s, Pageable pageable);
	
}
