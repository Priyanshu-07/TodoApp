package com.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.todo.model.Users;

public interface UserRepo extends JpaRepository<Users, Long> {

	Users findByEmail(String email);
	
	@Query("SELECT u.firstName FROM Users u WHERE u.email = ?1")
	String findFirstNameByUsername(String userName);

}