package com.todo.todo.service;

import com.todo.todo.exception.ResourceNotFoundException;
import com.todo.todo.entity.Todo;
import com.todo.todo.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoServiceImpl implements TodoService {

    @Autowired
    private TodoRepository todoRepository;


    @Override
    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    @Override
    public Todo getTodoById(Long id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found with id " + id));
    }

    @Override
    public Todo createOrUpdateTodo(Todo todo) {
        return todoRepository.save(todo);
    }

    @Override
    public void deleteTodoById(Long id) {
        if (!todoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Todo not found with id " + id);
        }
        todoRepository.deleteById(id);
    }

}
