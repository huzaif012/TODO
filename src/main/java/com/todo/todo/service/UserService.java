package com.todo.todo.service;

import com.todo.todo.entity.User;
import com.todo.todo.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    public User getUserByUsernmae(String username);
    public User SaveUser(User user);
    public boolean existByUsername(String username);



}
