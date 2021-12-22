package com.pizza.project.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pizza.project.entities.User;

@Repository
public interface SecureUserRepository extends JpaRepository<User, Integer> {

	boolean existsByUsername(String username);

	Optional<User> findByUsername(String username);

	void deleteByUsername(String username);

	Optional<User> findByEmail(String value);

}