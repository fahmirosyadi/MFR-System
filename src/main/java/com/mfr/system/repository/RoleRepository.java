package com.mfr.system.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mfr.system.entity.Role;

public interface RoleRepository extends CommonRepository<Role, Long> {
	public Role findByRole(String role);
	@Override
	@Query(value = "select * from role where role like %:s%", nativeQuery = true)
	public Page<Role> findLike(@Param("s") String s, Pageable pageable);
}