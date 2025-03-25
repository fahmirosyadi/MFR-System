package com.ta.sia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ta.sia.entity.JurnalDetails;

public interface JurnalDetailsRepository extends JpaRepository<JurnalDetails, Long> {

	@Query(value = "delete from jurnal_details where jurnal_id = ?", nativeQuery = true)
	public void deleteByJurnal(Long jurnalId);
	
}
