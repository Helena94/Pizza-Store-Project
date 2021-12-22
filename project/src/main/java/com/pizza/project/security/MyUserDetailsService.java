package com.pizza.project.security;

import com.pizza.project.entities.User;
import com.pizza.project.exception.ModelException;
import com.pizza.project.repositories.SecureUserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {
	@Autowired
	private SecureUserRepository secureUserRepository;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		User user = secureUserRepository.findByUsername(userName).orElseThrow(() -> new ModelException("User not found",
				HttpStatus.NOT_FOUND, String.format("User by %s doesn't exist ", userName)));

		return org.springframework.security.core.userdetails.User.withUsername(userName).password(user.getPassword())
				.authorities(user.getRole()).accountExpired(false).accountLocked(false).credentialsExpired(false)
				.disabled(false).build();
	}

}