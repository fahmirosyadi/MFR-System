package com.ta.sia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ta.sia.entity.Rekening;

import java.util.List;

@Repository
public interface RekeningRepository extends JpaRepository<Rekening, Long> {
	
//	List<Rekening> findByHeader(boolean header);
	List<Rekening> findByParent(Rekening parent);
	List<Rekening> findByJenis(String jenis);
	
//	@Query("SELECT r FROM Rekening r left join fetch r.jurnalDetails jd where r.jenis = ?1 and jd.jurnal.tanggal <= ?2")
//	List<Rekening> findByJenis(String jenis, Date periode);
	List<Rekening> findByNomor(String nomor);
	List<Rekening> findByChild(Rekening rekening);

	@Transactional
	@Modifying
	@Query(value = "DELETE FROM Rekening r WHERE r.id = :id")
	void deleteById(@Param("id") Long id);

}
