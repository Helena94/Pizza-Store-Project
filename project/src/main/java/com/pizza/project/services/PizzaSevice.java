package com.pizza.project.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pizza.project.dtos.PizzaRequest;
import com.pizza.project.dtos.PizzaResponse;
import com.pizza.project.dtos.PizzaUpdateRequest;
import com.pizza.project.entities.Pizza;
import com.pizza.project.exception.ModelException;
import com.pizza.project.repositories.PizzaRepository;

@Service
@Transactional
public class PizzaSevice {

	@Autowired
	private PizzaRepository pizzaRepository;

	@Transactional(readOnly = true)
	public List<PizzaResponse> findAll() {
		return pizzaRepository.findAll().stream().map(PizzaSevice::map).toList();
	};

	@Transactional(readOnly = true)
	public List<PizzaResponse> findByName(String name) {
		return pizzaRepository.findByNameContains(name).stream().map(PizzaSevice::map).toList();
	};

	@Transactional(readOnly = true)
	public PizzaResponse findBySlug(String slug) {
		return pizzaRepository.findBySlug(slug).map(PizzaSevice::map)
				.orElseThrow(() -> new ModelException("Pizza not found", HttpStatus.NOT_FOUND,
						String.format("There is no pizza by slug %s.", slug)));
	};

	public void save(PizzaRequest pizzaDTO) {
		Pizza pizza = map(pizzaDTO);
		pizza.setDate(new Date());
		pizzaRepository.save(pizza);
	};

	public PizzaResponse updateBySlug(String slug, PizzaUpdateRequest pizzaDTO) {
		Pizza pizza = this.pizzaRepository.findBySlug(slug).orElseThrow(() -> new ModelException("Pizza not found",
				HttpStatus.NOT_FOUND, String.format("There is no pizza by slug %s.", slug)));
		update(pizzaDTO, pizza);
		return map(pizza);
	};

	public void deleteBySlug(String slug) {
		Pizza pizza = this.pizzaRepository.findBySlug(slug).orElseThrow(() -> new ModelException("Pizza not found",
				HttpStatus.NOT_FOUND, String.format("There is no pizza by slug %s.", slug)));
		pizzaRepository.delete(pizza);
	};

	private void update(PizzaUpdateRequest pizzaDTO, Pizza pizza) {
		if (!pizza.getSlug().equals(pizzaDTO.getSlug())
				&& this.pizzaRepository.findBySlug(pizzaDTO.getSlug()).isPresent()) {
			throw new ModelException("Slug already used.", HttpStatus.CONFLICT,
					String.format("Pizza by slug %s already exists.", pizzaDTO.getSlug()));
		}
		updatePizza(pizza, pizzaDTO);
		this.pizzaRepository.save(pizza);
	}

	private static void updatePizza(Pizza pizza, PizzaUpdateRequest pizzaDTO) {
		pizza.setName(pizzaDTO.getName());
		pizza.setSlug(pizzaDTO.getSlug());
		pizza.setSize(pizzaDTO.getSize());
		pizza.setPrice(pizzaDTO.getPrice());

		pizza.setDate(new Date());
	}

	private static PizzaResponse map(Pizza pizza) {

		return PizzaResponse.builder().name(pizza.getName()).price(pizza.getPrice()).slug(pizza.getSlug())
				.size(pizza.getSize()).date(pizza.getDate()).build();

	}

	private static Pizza map(PizzaRequest pizza) {

		return Pizza.builder().name(pizza.getName()).slug(pizza.getSlug()).size(pizza.getSize()).price(pizza.getPrice())
				.build();

	}

}
