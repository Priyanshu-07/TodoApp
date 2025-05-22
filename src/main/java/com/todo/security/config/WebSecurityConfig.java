package com.todo.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

@Configuration
public class WebSecurityConfig {

	@Autowired
	UserDetailsService userDetailsService;

	@Bean
	SecurityContextRepository securityContextRepository() {
		return new DelegatingSecurityContextRepository(new RequestAttributeSecurityContextRepository(),
				new HttpSessionSecurityContextRepository());
	}

	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager authenticationManager() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return new ProviderManager(authProvider);
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable());
//		http.httpBasic(Customizer.withDefaults());
//		http.formLogin(Customizer.withDefaults());
		http.authorizeHttpRequests((authz) -> authz
				.requestMatchers(HttpMethod.GET, "/api/todos/add", "/api/todos", "/api/todos/update/{id}",
						"/api/todos/delete/{id}")
				.hasAnyRole("ADMIN")

				.requestMatchers(HttpMethod.POST, "/api/todos/add", "/api/todos/update/{id}", "/api/todos/delete/{id}")
				.hasAnyRole("ADMIN")

				.requestMatchers("/", "/login", "/showregistration", "/registerUser").permitAll()
				

				.anyRequest().authenticated());
		
		http.formLogin(form -> form
				.loginPage("/") // Specify the login page URL
				.loginProcessingUrl("/login") // Specify the password parameter name
				.defaultSuccessUrl("/api/todos") // Redirect to /api/todos after successful login
				.failureUrl("/?error=true") // Redirect to /login with error message on failure
				.permitAll() // Allow all users to access the login page
		);

		http.logout(logout -> logout.logoutUrl("/logout") // Specify the logout URL
				.logoutSuccessUrl("/") // Redirect to /login after logout
				.invalidateHttpSession(true) // Invalidate the session
				.deleteCookies("JSESSIONID") // Delete cookies
		);
		return http.build();
	}
}