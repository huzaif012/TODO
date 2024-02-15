package com.todo.todo.controller;

import com.todo.todo.dto.TodoDto;
import com.todo.todo.entity.Todo;
import com.todo.todo.entity.User;
import com.todo.todo.service.TodoService;
import com.todo.todo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
        // Fetch todos associated with the logged-in user
        List<Todo> todos = todoService.findByUser(user);


        // Return the list of todos in the response
        return ResponseEntity.ok(todos);
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<?> updateTodoForLoggedInUser(Authentication authentication,@PathVariable Long id, @RequestBody TodoDto todoDto){
        //Get the username of the logged in user from authentication
        String username = authentication.getName();

        //Fetch user details from the database based on the username
        User user = userService.getUserByUsername(username);
        if(user == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        //Fetching the todo
        Todo todo = todoService.getTodoById(id);
        if(todo == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No task found");
        }
        //checking if todo task belong to the logged in user
        if(!todo.getUser().equals(user)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not authorize to update the todo");
        }

        //update the existing todo task
        todo.setTitle(todoDto.getTitle());
        todo.setCompleted(todoDto.isCompleted());

        //Save the updated todo
        todoService.createOrUpdateTodo(todo);

        return ResponseEntity.ok("Todo updated successfully");
    }
    @DeleteMapping("/user/{id}")
    public  ResponseEntity<?> DeletTodoForloggedInUser(Authentication authentication,@PathVariable Long id){
        //Get the username of the logged- in user from authentication
        String username = authentication.getName();

        //Fetch user details from the database based on the username
        User user = userService.getUserByUsername(username);
        if(user == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        // Fetch the existing todo task
        Todo existingTodo = todoService.getTodoById(id);
        if (existingTodo == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Todo task not found");
        }

        // Check if the todo task belongs to the logged-in user
        if (!existingTodo.getUser().equals(user)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to delete this todo task");
        }

        //deleting todo
        todoService.deleteTodoById(id);
        return ResponseEntity.ok("Deleted todo successfully");
    }

}
