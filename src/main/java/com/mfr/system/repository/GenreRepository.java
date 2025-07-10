package com.mfr.system.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mfr.system.entity.Genre;

public interface GenreRepository extends CommonRepository<Genre, Long> {
	public Genre findByGenre(String genre);
	@Override
	@Query(value = "select * from genre where genre like CONCAT('%', :s, '%')", nativeQuery = true)
	public Page<Genre> findLike(@Param("s") String s, Pageable pageable);
}