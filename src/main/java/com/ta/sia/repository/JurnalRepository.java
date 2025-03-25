package com.ta.sia.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.ta.sia.entity.Jurnal;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JurnalRepository extends CommonRepository<Jurnal, Long> {
	
	@Query(value = "SELECT * FROM jurnal ORDER BY id DESC LIMIT 1", nativeQuery = true)
	public Jurnal findLast();
	
	@Override
	@Query(value = "select * from jurnal where keterangan like %:s%", nativeQuery = true)
	public Page<Jurnal> findLike(@Param("s") String s, Pageable pageable);
	
	@Query(value = "SELECT * FROM jurnal ORDER BY tanggal DESC LIMIT 1", nativeQuery = true)
	public Jurnal findLastByDate();
	
	@Query(value = "select j from Jurnal j where j.tanggal <= ?1")
	public List<Jurnal> findByTanggal(Date tanggal);
	
	public List<Jurnal> findByJenis(String jenis, Sort sort);
	
}
