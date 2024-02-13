package com.todo.todo.controller;
import com.todo.todo.dto.TodoDto;
import com.todo.todo.entity.Todo;
import com.todo.todo.entity.User;
import com.todo.todo.repository.TodoRepository;
import com.todo.todo.service.TodoService;
import com.todo.todo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    @Autowired
    private TodoService todoService;
    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<?> createTodo(Authentication authentication, @RequestBody TodoDto todoDto) {
        // Get the username of the logged-in user from authentication
        String username = authentication.getName();

        // Fetch user details using the UserDetailService
        User user = userService.getUserByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        // Create todo task and associate it with the user
        Todo todo = new Todo();
        todo.setTitle(todoDto.getTitle());
        todo.setCompleted(todoDto.isCompleted());
        todo.setUser(user);

        // Save the todo task
        todoService.createOrUpdateTodo(todo);

        return ResponseEntity.ok("Todo task created successfully");
    }
    @GetMapping("/user")
    public ResponseEntity<?> getTodosForLoggedInUser(Authentication authentication) {
        // Get the username of the logged-in user from authentication
        String username = authentication.getName();

        // Fetch user details from the database based on the username
        User user = userService.getUserByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }
        System.out.println("user:"+username);
        // Fetch todos associated with the logged-in user
        List<Todo> todos = todoService.findByUser(user);
        System.out.println("Todo size:"+todos.size());


        // Return the list of todos in the response
        return ResponseEntity.ok(todos);
    }
//    @GetMapping
//    public ResponseEntity<List<Todo>> getAllTodos() {
//        List<Todo> todos = todoService.getAllTodos();
//        return new ResponseEntity<>(todos, HttpStatus.OK);
//    }
//
//    @PostMapping
//    public ResponseEntity<Todo> createTodo(@RequestBody Todo todo) {
//        Todo createdTodo = todoService.createOrUpdateTodo(todo);
//        return new ResponseEntity<>(createdTodo, HttpStatus.CREATED);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Todo> getTodoById(@PathVariable Long id) {
//        Todo todo = todoService.getTodoById(id);
//        return new ResponseEntity<>(todo, HttpStatus.OK);
//    }
//    @PutMapping("/{id}")
//    public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @RequestBody Todo todo) {
//        todo.setId(id);
//        Todo updatedTodo = todoService.createOrUpdateTodo(todo);
//        return new ResponseEntity<>(updatedTodo, HttpStatus.OK);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
//        todoService.deleteTodoById(id);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
}
