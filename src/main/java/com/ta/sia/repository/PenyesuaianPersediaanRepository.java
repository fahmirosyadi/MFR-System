package com.ta.sia.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ta.sia.entity.PenyesuaianPersediaan;

public interface PenyesuaianPersediaanRepository extends CommonRepository<PenyesuaianPersediaan, Long> {

	@Override
	@Query(value = "select * from barang_keluar p join transaksi t on t.id = p.id where t.keterangan like %:s%", nativeQuery = true)
	public Page<PenyesuaianPersediaan> findLike(@Param("s") String s, Pageable pageable);

}
