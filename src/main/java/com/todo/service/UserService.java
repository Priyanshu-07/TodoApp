package com.todo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.todo.repository.UserRepo;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepository;

    public String getFirstNameByUsername(String userName) {
        return userRepository.findFirstNameByUsername(userName);
    }
}