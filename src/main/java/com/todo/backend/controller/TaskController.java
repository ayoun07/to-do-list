package com.todo.backend.controller;

import com.todo.backend.model.Task;
import com.todo.backend.model.User;
import com.todo.backend.model.ListEntity;
import com.todo.backend.repository.TaskRepository;
import com.todo.backend.repository.UserRepository;
import com.todo.backend.repository.ListRepository;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin
public class TaskController {

    private final TaskRepository taskRepo;
    private final UserRepository userRepo;
    private final ListRepository listRepo;

    // Injection des dépendances via le constructeur
    public TaskController(TaskRepository taskRepo, UserRepository userRepo, ListRepository listRepo) {
        this.taskRepo = taskRepo;
        this.userRepo = userRepo;
        this.listRepo = listRepo;
    }

    // Récupère les tâches d'une liste donnée, seulement si elle appartient à l'utilisateur connecté
    @GetMapping("/list/{listId}")
    public List<Task> getTasksByList(@PathVariable Long listId, Authentication auth) {
        String username = auth.getName();
        User user = userRepo.findByUsername(username).orElseThrow();
        ListEntity list = listRepo.findById(listId).orElseThrow();

        if (!list.getUser().equals(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accès interdit.");
        }

        return taskRepo.findByListAndUser(list, user);
    }

    // Récupère toutes les tâches de l'utilisateur connecté
    @GetMapping
    public List<Task> getTasks(Authentication auth) {
        User user = userRepo.findByUsername(auth.getName()).orElseThrow();
        return taskRepo.findByUser(user);
    }

    // Ajoute une nouvelle tâche pour l'utilisateur connecté (et la lie à une liste si présente)
    @PostMapping
    public Task addTask(@RequestBody Task task, Authentication auth) {
        User user = userRepo.findByUsername(auth.getName()).orElseThrow();
        task.setUser(user);

        if (task.getList() != null) {
            ListEntity list = listRepo.findById(task.getList().getId())
                .orElseThrow(() -> new RuntimeException("Liste introuvable"));
            task.setList(list);
        }

        return taskRepo.save(task);
    }

    // Met à jour une tâche (titre + état terminé)
    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody Task updatedTask) {
        return taskRepo.findById(id).map(task -> {
            task.setTitle(updatedTask.getTitle());
            task.setCompleted(updatedTask.getCompleted());
            return taskRepo.save(task);
        }).orElseThrow();
    }

    // Supprime une tâche par son id
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskRepo.deleteById(id);
    }
}
