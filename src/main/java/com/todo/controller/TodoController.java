package com.todo.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.todo.model.Todo;
import com.todo.service.TodoService;
import com.todo.service.UserService;

@Controller
@RequestMapping("/api/todos")
public class TodoController {

	@Autowired
	private TodoService todoService;

	@Autowired
	private UserService userService;

	@GetMapping()
	public ModelAndView getAllTodos(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size, Principal principal) {
		ModelAndView mv = new ModelAndView();
		Pageable pageable = PageRequest.of(page, size);
//		Page<Todo> todoPage = todoService.getAllTodos(pageable);
		Page<Todo> todoPage = todoService.getTodosByUsername(principal.getName(), pageable);
		String firstName = userService.getFirstNameByUsername(principal.getName());
		mv.setViewName("index");
		mv.addObject("todos", todoPage.getContent());
		mv.addObject("currentPage", page);
		mv.addObject("totalPages", todoPage.getTotalPages());
		mv.addObject("firstName", firstName);
		return mv;
	}

	@GetMapping("/{id}")
	public Todo getTodoById(@PathVariable Long id, Principal principal) {
		return todoService.getTodoById(id, principal.getName());
	}

	@GetMapping("/add")
	public ModelAndView showAddTodoForm() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("AddForm");
		mv.addObject("todo", new Todo());
		return mv;
	}

	@PostMapping("/add")
	public ModelAndView createTodo(@ModelAttribute Todo todo, Principal principal) {
		ModelAndView mv = new ModelAndView();
		todo.setUserName(principal.getName());
		todoService.createTodo(todo);
		mv.setViewName("redirect:/api/todos");
		return mv;
	}

	@GetMapping("/update/{id}")
	public ModelAndView showEditTodoForm(@PathVariable Long id) {
		ModelAndView mv = new ModelAndView();
		Todo todo = todoService.getTodoById(id);
		mv.setViewName("UpdateForm");
		mv.addObject("todo", todo);
		return mv;
	}

	@PostMapping("/update/{id}")
	public ModelAndView updateTodo(@PathVariable Long id, @ModelAttribute Todo todo) {
		todoService.updateTodo(id, todo);
		return new ModelAndView("redirect:/api/todos");
	}

	@DeleteMapping("/delete/{id}")
	public ModelAndView deleteTodo(@PathVariable Long id) {
		Todo todo = todoService.getTodoById(id);
		if (todo == null) {
			return new ModelAndView("redirect:/api/todos");
		} else {
			todoService.deleteTodo(todo);
			return new ModelAndView("redirect:/api/todos");
		}
	}

}
