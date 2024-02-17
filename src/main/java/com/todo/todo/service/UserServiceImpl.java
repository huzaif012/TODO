package com.todo.todo.service;

import com.todo.todo.entity.User;
import com.todo.todo.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepo userRepository;

    @Override
    public User getUserByUsernmae(String username) {
        return userRepository.findByEmail(username);
    }

    @Override
    public User SaveUser(User user){
        User userdto = userRepository.save(user);
        return userdto;
    }
    @Override
    public boolean existByUsername(String username){
        return userRepository.existsByEmail(username);
    }
}
