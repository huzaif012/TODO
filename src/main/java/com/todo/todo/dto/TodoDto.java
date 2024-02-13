package com.todo.todo.dto;

public class TodoDto {
    private String title;
    private boolean completed;

    // Default constructor
    public TodoDto() {}

    // Parameterized constructor
    public TodoDto(String title, boolean completed) {
        this.title = title;
        this.completed = completed;
    }

    // Getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}

