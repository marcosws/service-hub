package com.github.marcosws.servicehub.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.marcosws.servicehub.automotive.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	
	Optional<User> findByEmail(String email);
	
	boolean existsByEmail(String email);
	

}
