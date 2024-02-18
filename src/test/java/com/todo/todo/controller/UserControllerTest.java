package com.todo.todo.controller;

import com.todo.todo.dto.UserDto;
import com.todo.todo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        // Reset Mockito mocks before each test
        reset(userService, passwordEncoder);
    }

    @Test
    void testRegisterUser_UserExists() {
        // Mock behavior to simulate existing user
        when(userService.existByUsername(anyString())).thenReturn(true);

        // Create a sample UserDto
        UserDto userDto = new UserDto();
        userDto.setFirstname("John");
        userDto.setLastname("Doe");
        userDto.setEmail("john@example.com");
        userDto.setPassword("password123");

        // Call the method under test
        ResponseEntity<?> response = userController.RegisterUser(userDto);

        // Verify the response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("User with this email already exist", response.getBody());

        // Verify that userService.existByUsername() is called with the correct email
        verify(userService).existByUsername("john@example.com");
        // Verify that userService.SaveUser() is not called
        verify(userService, never()).SaveUser(any());
    }

    @Test
    void testRegisterUser_Postmapping(){
        UserDto userDto = new UserDto();
        userDto.setFirstname("John");
        userDto.setLastname("Doe");
        userDto.setEmail("john@example.com");
        userDto.setPassword("password123");

        ResponseEntity<?> response = userController.RegisterUser(userDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User registration has successfully completed",response.getBody());
    }

}
