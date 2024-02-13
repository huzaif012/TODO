package com.todo.todo.repository;

import com.todo.todo.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Roles,Long> {
}
