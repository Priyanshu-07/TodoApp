package com.todo.controller;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.todo.model.Role;
import com.todo.model.Users;
import com.todo.repository.UserRepo;
import com.todo.security.SecurityService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class UserLoginController {
	
	@Autowired
	private SecurityService securityService;
	
	@Autowired
	private UserRepo userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping("/showregistration")
	public String showregistrationPage()
	{
		return "registerUser";
	}
	
	@PostMapping("/registerUser")
	public String registerUser(Users user) {
		
		Set<Role> roles = new HashSet<>();
		Role role = new Role();
		role.setId(1L);
		role.setName("ROLE_USER");
		roles.add(role);
		user.setRoles(roles);
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userRepository.save(user);
		return "login";
	}
	
	@GetMapping("/")
	public String showLoginPage() {
		return "login";
	}
	
	@PostMapping("/login")
	public String login(@RequestParam String email, @RequestParam String password, HttpServletRequest request, HttpServletResponse response) {
		boolean login = securityService.login(email, password, request, response);
		if (login) {
			return "redirect:/api/todos";
		}
		
		return "redirect:/?error=true";
	}

}