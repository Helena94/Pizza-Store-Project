package com.pizza.project.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pizza.project.entities.Pizza;

@Repository
public interface PizzaRepository extends JpaRepository<Pizza, Long> {
	List<Pizza> findAllByOrderByDateDesc();

	List<Pizza> findByName(String name);

	Optional<Pizza> findBySlug(String slug);

	List<Pizza> findByNameContains(String name);
}
