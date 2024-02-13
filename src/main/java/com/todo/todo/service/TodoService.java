package com.todo.todo.service;

import com.todo.todo.entity.Todo;

import java.util.List;

public interface TodoService {
    List<Todo> getAllTodos();

    Todo getTodoById(Long id);

    Todo createOrUpdateTodo(Todo todo);

    void deleteTodoById(Long id);
}
