package com.ta.sia.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ta.sia.entity.Customer;
import com.ta.sia.entity.Supplier;

public interface CustomerRepository extends CommonRepository<Customer, Long> {
	@Query(value = "select * from customer where nama like %:s% or alamat like %:s% or telp like %:s%", nativeQuery = true)
	public Page<Customer> findLike(@Param("s") String s, Pageable pageable);
}
