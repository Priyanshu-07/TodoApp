package com.todo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class SecurityServiceImpl implements SecurityService {
	
	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	SecurityContextRepository securityContextRepository;

	@Override
	public boolean login(String username, String password, HttpServletRequest request, HttpServletResponse response) {
		UserDetails userByUsername = userDetailsService.loadUserByUsername(username);
		if (userByUsername == null) {
			return false;
		}
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userByUsername, password, userByUsername.getAuthorities());
		authenticationManager.authenticate(authenticationToken);
		boolean result = authenticationToken.isAuthenticated();
		if (result) {
			// Set the authentication in the security context
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			securityContextRepository.saveContext(SecurityContextHolder.getContext(), request, response);
		}
		
		return result;
	}

}