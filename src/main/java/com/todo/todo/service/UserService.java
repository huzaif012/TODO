package com.todo.todo.service;

import com.todo.todo.entity.User;
import com.todo.todo.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepository;

    public User getUserByUsername(String username) {
        return userRepository.findByEmail(username);
    }
}
