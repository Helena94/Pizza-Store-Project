package com.pizza.project.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter

public class User implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Setter
	@Column(unique = true, nullable = false)
	private String username;
	@Setter
	@Column(unique = true, nullable = false)
	private String email;
	@Setter
	@Column(unique = true, nullable = false)
	private String password;
	@Setter
	@Enumerated(EnumType.STRING)
	private Role role = Role.ROLE_USER;

}