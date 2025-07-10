package com.mfr.system.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mfr.system.entity.Song;

public interface SongRepository extends CommonRepository<Song, Long> {
	public Song findBySong(String song);
	@Override
	@Query(value = "select * from song where Song like CONCAT('%', :s, '%')", nativeQuery = true)
	public Page<Song> findLike(@Param("s") String s, Pageable pageable);
}