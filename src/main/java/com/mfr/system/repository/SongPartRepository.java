package com.mfr.system.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mfr.system.entity.SongPart;

public interface SongPartRepository extends CommonRepository<SongPart, Long> {
	public SongPart findByPart(String chord);
	@Override
	@Query(value = "select * from chord where part like %:s%", nativeQuery = true)
	public Page<SongPart> findLike(@Param("s") String s, Pageable pageable);
}