package com.ta.sia.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ta.sia.entity.Jurnal;
import com.ta.sia.entity.Pembelian;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PembelianRepository extends CommonRepository<Pembelian, Long> {
	
	@Query(value = "SELECT * FROM pembelian p join transaksi t on t.id = p.id ORDER BY p.id DESC LIMIT 1", nativeQuery = true)
	public Pembelian findLast();

	@Query(value = "select p from Pembelian p where p.tanggal <= ?1")
	public List<Pembelian> findByTanggal(Date periode);
	
	@Override
	@Query(value = "select * from pembelian p join transaksi t on t.id = p.id where t.keterangan like %:s%", nativeQuery = true)
	public Page<Pembelian> findLike(@Param("s") String s, Pageable pageable);
	
}
