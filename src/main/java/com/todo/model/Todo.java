package com.todo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Todo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private boolean completed = false;
	
	private String userName;

	public Todo() {
		super();
		// TODO Auto-generated constructor stub
	}

	

	public Todo(Long id, String title, boolean completed, String userName) {
		super();
		this.id = id;
		this.title = title;
		this.completed = completed;
		this.userName = userName;
	}



	public String getUserName() {
		return userName;
	}



	public void setUserName(String userName) {
		this.userName = userName;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	
	
	@Override
	public String toString() {
		return "Todo [id=" + id + ", title=" + title + ", completed=" + completed +", userName= "+ userName + "]";
	}
	

}