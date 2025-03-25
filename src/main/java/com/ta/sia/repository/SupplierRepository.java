package com.ta.sia.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.ta.sia.entity.Role;
import com.ta.sia.entity.Supplier;

public interface SupplierRepository extends CommonRepository<Supplier, Long>, JpaSpecificationExecutor<Supplier>{

	@Query(value = "select s.id, s.nama, s.alamat, s.telp, s.saldo_awal from supplier s", nativeQuery = true)
	public List<Supplier> getAll();
	
	@Query(value = "select * from supplier where nama like %:s% or alamat like %:s% or telp like %:s%", nativeQuery = true)
	public Page<Supplier> findLike(@Param("s") String s, Pageable pageable);
	
}
