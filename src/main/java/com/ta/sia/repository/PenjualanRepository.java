package com.ta.sia.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ta.sia.entity.Pembelian;
import com.ta.sia.entity.Penjualan;

public interface PenjualanRepository extends CommonRepository<Penjualan, Long> {

	@Query(value = "select p from Penjualan p where p.tanggal <= ?1")
	List<Penjualan> findBeforeTanggal(Date periode);
	
	@Query(value = "select p from Penjualan p where p.tanggal > ?1")
	List<Penjualan> findAfterTanggal(Date periode);

	@Override
	@Query(value = "select * from penjualan p join transaksi t on t.id = p.id where t.keterangan like %:s%", nativeQuery = true)
	public Page<Penjualan> findLike(@Param("s") String s, Pageable pageable);
}
