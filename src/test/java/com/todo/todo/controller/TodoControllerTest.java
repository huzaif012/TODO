package com.todo.todo.controller;

import com.mysql.cj.x.protobuf.Mysqlx;
import com.todo.todo.dto.TodoDto;
import com.todo.todo.dto.UserDto;
import com.todo.todo.entity.Todo;
import com.todo.todo.entity.User;
import com.todo.todo.service.TodoService;
import com.todo.todo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TodoControllerTest {

    @InjectMocks
    private TodoController todoController;
    @Mock
    private TodoService todoService;

    @Mock
    private UserService userService;

    @BeforeEach
    public void setup(){
        User user = new User();
        user.setFirstname("John");
        user.setLastname("Doe");
        user.setEmail("john@example.com");
        user.setPassword("password123");
        when(userService.getUserByUsernmae("john@example.com")).thenReturn(user);


    }

    @Test
    void createTodo() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("john@example.com", "password123");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TodoDto todoDto = new TodoDto();
        todoDto.setTitle("Complete Homework");
        todoDto.isCompleted();

        ResponseEntity<?> response = todoController.createTodo(authentication,todoDto);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("Todo task created successfully",response.getBody());
    }
    @Test
    void createTodo_userUnauthorized() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("john@example.com", "password123");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(userService.getUserByUsernmae("john@example.com")).thenReturn(null);

        TodoDto todoDto = new TodoDto();
        todoDto.setTitle("Complete Homework");
        todoDto.isCompleted();

        ResponseEntity<?> response = todoController.createTodo(authentication,todoDto);

        assertEquals(HttpStatus.UNAUTHORIZED,response.getStatusCode());
        assertEquals("User not found",response.getBody());
    }

    @Test
    void getTodosForLoggedInUser() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("john@example.com", "password123");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResponseEntity<?> response = todoController.getTodosForLoggedInUser(authentication);

        assertEquals(HttpStatus.OK,response.getStatusCode());

    }
    @Test
    void getTodosForLoggedInUser_userUnauthorized() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("john@example.com", "password123");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(userService.getUserByUsernmae("john@example.com")).thenReturn(null);

        ResponseEntity<?> response = todoController.getTodosForLoggedInUser(authentication);

        assertEquals(HttpStatus.UNAUTHORIZED,response.getStatusCode());

    }

    @Test
    void updateTodoForLoggedInUser() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("john@example.com", "password123");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = new User();
        user = userService.getUserByUsernmae("john@example.com");

        // Mocking the behavior of TodoService to return a todo
        Todo todo = new Todo();
        todo.setUser(user); // Setting the user same as the authenticated user
        when(todoService.getTodoById(1L)).thenReturn(todo);

        TodoDto updatedTodoDto = new TodoDto();
        updatedTodoDto.setTitle("Updated Task");
        updatedTodoDto.setCompleted(true);

        ResponseEntity<?> response = todoController.updateTodoForLoggedInUser(authentication, 1L, updatedTodoDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Todo updated successfully", response.getBody());

        // Verifying that the todo details are updated
        assertEquals(updatedTodoDto.getTitle(), todo.getTitle());
        assertEquals(updatedTodoDto.isCompleted(), todo.isCompleted());

        // Verifying that todoService.createOrUpdateTodo is called with the updated todo
        verify(todoService).createOrUpdateTodo(todo);
    }
    @Test
    void updateTodoForLoggedInUser_userUnauthorized() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("john@example.com", "password123");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(userService.getUserByUsernmae("john@example.com")).thenReturn(null);


        ResponseEntity<?> response = todoController.updateTodoForLoggedInUser(authentication,1L,new TodoDto());

        assertEquals(HttpStatus.UNAUTHORIZED,response.getStatusCode());
        assertEquals("User not found",response.getBody());

    }
    @Test
    void testUpdateTodoForLoggedInUser_TodoNotFound() {
        // Mocking the authentication object
        Authentication authentication = new UsernamePasswordAuthenticationToken("john@example.com", "password123");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Mocking the behavior of TodoService to return null when getTodoById is called
        when(todoService.getTodoById(1L)).thenReturn(null);

        // Calling the method under test
        ResponseEntity<?> response = todoController.updateTodoForLoggedInUser(authentication, 1L, new TodoDto());

        // Asserting the response
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No task found", response.getBody());
    }

    @Test
    void testUpdateTodoForLoggedInUser_ForbiddenToUpdate() {
        // Mocking the authentication object
        Authentication authentication = new UsernamePasswordAuthenticationToken("john@example.com", "password123");

        // Mocking the behavior of TodoService to return a todo
        Todo todo = new Todo();
        todo.setUser(new User()); // Setting a different user than the authenticated user
        when(todoService.getTodoById(1L)).thenReturn(todo);

        // Calling the method under test
        ResponseEntity<?> response = todoController.updateTodoForLoggedInUser(authentication, 1L, new TodoDto());

        // Asserting the response
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Not authorize to update the todo", response.getBody());
    }

    @Test
    void deletTodoForloggedInUser() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("john@example.com", "password123");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = new User();
        user = userService.getUserByUsernmae("john@example.com");
        Todo existingTodo = new Todo();
        existingTodo.setId(1L);
        existingTodo.setUser(user);
        when(todoService.getTodoById(1L)).thenReturn(existingTodo);

        // Perform delete operation
        ResponseEntity<?> response = todoController.DeletTodoForloggedInUser(authentication, 1L);

        // Verify that todoService.deleteTodoById(1L) is called
        verify(todoService).deleteTodoById(1L);
        // Verify response status and message
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Deleted todo successfully", response.getBody());
    }
    @Test
    public void testDeleteTodoForLoggedInUser_Unauthorized() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("john@example.com", "password123");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(userService.getUserByUsernmae("john@example.com")).thenReturn(null);

        ResponseEntity<?> response = todoController.DeletTodoForloggedInUser(authentication, 1L);

        assertEquals(HttpStatus.UNAUTHORIZED,response.getStatusCode());
        assertEquals("User not found",response.getBody());
    }
    @Test
    public void testDeleteTodoForLoggedInUser_TodoNotFound() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("john@example.com", "password123");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(todoService.getTodoById(1L)).thenReturn(null);

        ResponseEntity<?> response = todoController.DeletTodoForloggedInUser(authentication,1L);

        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
        assertEquals("Todo task not found",response.getBody());
    }
    @Test
    void deleteTodoForLoggedInUser_NotAuthorized() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("john@example.com", "password123");

        // Mocking the behavior of TodoService to return a todo
        Todo todo = new Todo();
        todo.setUser(new User()); // Setting a different user than the authenticated user
        when(todoService.getTodoById(1L)).thenReturn(todo);

        ResponseEntity<?> response = todoController.DeletTodoForloggedInUser(authentication,1L);

        assertEquals(HttpStatus.FORBIDDEN,response.getStatusCode());
        assertEquals("You are not authorized to delete this todo task",response.getBody());
    }
}