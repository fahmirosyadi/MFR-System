package com.ta.sia.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ta.sia.entity.Menu;

@Repository
public interface MenuRepository extends CommonRepository<Menu, Long>{

	@Query(value = "select * from menu m where (name like %:s% or url like %:s%) and parent_id is null order by sort asc", nativeQuery = true)
	public List<Menu> findLike(@Param("s") String s);
	
	@Override
	@Query(value = "select * from menu m where (name like %:s% or url like %:s%) and parent_id is null order by sort asc", nativeQuery = true)
	public Page<Menu> findLike(@Param("s") String s, Pageable pageable);
	
	public List<Menu> findByParent(Menu parent);
	public Menu findByName(String name);
	 
}
