package com.todo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.todo.model.Todo;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long>, PagingAndSortingRepository<Todo, Long> {
	
	Todo findTodoById(Long id);
		

	@Query("SELECT t FROM Todo t WHERE t.userName = ?1")
	Page<Todo> findByUsername(String userName, Pageable pageable);
	
	
}