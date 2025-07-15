package com.todo.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.todo.backend.model.ListEntity;
import com.todo.backend.model.Task;
import com.todo.backend.model.User;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUser(User user);
    List<Task> findByListAndUser(ListEntity list, User user);
} 
