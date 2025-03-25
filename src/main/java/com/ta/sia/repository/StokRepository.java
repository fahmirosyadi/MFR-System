package com.ta.sia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import com.ta.sia.entity.Stok;
import com.ta.sia.entity.Persediaan;

public interface StokRepository extends JpaRepository<Stok, String> {

	List<Stok> findByPersediaan(Persediaan persediaan);
	
}
