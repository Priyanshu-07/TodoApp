package com.todo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import com.todo.model.Todo;
import com.todo.repository.TodoRepository;

@Service
public class TodoService {

	@Autowired
	private TodoRepository todoRepository;

	public Page<Todo> getAllTodos(Pageable pageable) {

		return todoRepository.findAll(pageable);
	}

	public Todo getTodoById(@PathVariable Long id) {
		return todoRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found with id: " + id));
	}

	public Todo createTodo(Todo todo) {
		return todoRepository.save(todo);
	}

	public void deleteTodo(Todo todo) {
		todoRepository.delete(todo);

	}

	public Todo updateTodo(Long id, Todo todo) {

		Todo existingTodo = todoRepository.findById(id).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found with id: " + todo.getId()));
		existingTodo.setTitle(todo.getTitle());
		existingTodo.setCompleted(todo.isCompleted());
		return todoRepository.save(existingTodo);
	}

	public List<Todo> getAllTodos() {
		return todoRepository.findAll();
	}

	public Todo getTodoById(Long id, String userName) {
		Todo todo = getTodoById(id); // existing method
		if (!todo.getUserName().equals(userName)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
		}
		return todo;
	}

	public Page<Todo> getTodosByUsername(String name, Pageable pageable) {
		// TODO Auto-generated method stub
		Page<Todo> todos = todoRepository.findByUsername(name, pageable);
		return todos;
	}

}