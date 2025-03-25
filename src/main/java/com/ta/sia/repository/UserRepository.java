package com.ta.sia.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ta.sia.entity.PasswordResetToken;
import com.ta.sia.entity.User;

public interface UserRepository extends CommonRepository<User, Long> {

//	@Query(value = "select from user where username = ?",nativeQuery = true)
	Optional<User> findByUsername(String username);
	Optional<User> findByEmail(String email);
	Optional<User> findByPasswordResetToken(PasswordResetToken passwordResetToken);
	Optional<User> findByPasswordResetToken(Long id);
	
	Optional<User> findByUsername(String username, Sort sort);
	
	@Query(value = "select * from users where username like %:s%", nativeQuery = true)
	public Page<User> findLike(@Param("s") String s, Pageable pageable);
}
