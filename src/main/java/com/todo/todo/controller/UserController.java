package com.todo.todo.controller;

import com.todo.todo.dto.UserDto;
import com.todo.todo.entity.User;
import com.todo.todo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public ResponseEntity<?> RegisterUser(@RequestBody UserDto userDto){
        //check if user already exist
        if(userService.existByUsername(userDto.getEmail())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with this email already exist");
        }
        String encryptedPassword = passwordEncoder.encode(userDto.getPassword());
        //Create the User entity
        User user = new User();
        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        user.setEmail(userDto.getEmail());
        user.setPassword(encryptedPassword);

        //save the user entity to the database
        userService.SaveUser(user);
        return ResponseEntity.ok("User registration has successfully completed");
    }
}
