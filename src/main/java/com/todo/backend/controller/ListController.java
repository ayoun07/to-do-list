package com.todo.backend.controller;

import com.todo.backend.model.ListEntity;
import com.todo.backend.model.User;
import com.todo.backend.repository.ListRepository;
import com.todo.backend.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lists")
public class ListController {

    @Autowired
    private ListRepository listRepo;

    @Autowired
    private UserRepository userRepo;

    @GetMapping
    public List<ListEntity> getUserLists(Authentication authentication) {
        String username = authentication.getName();
        User user = userRepo.findByUsername(username).orElseThrow();
        return listRepo.findByUser(user);
    }

    @PostMapping
    public ResponseEntity<?> createList(@RequestBody ListEntity list, Authentication authentication) {
        String username = authentication.getName();
        User user = userRepo.findByUsername(username).orElseThrow();
        list.setUser(user);
        ListEntity saved = listRepo.save(list);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteList(@PathVariable Long id, Principal principal) {
        Optional<ListEntity> listOpt = listRepo.findById(id);
        if (listOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ListEntity list = listOpt.get();
        if (!list.getUser().getUsername().equals(principal.getName())) {
            return ResponseEntity.status(403).body("Accès interdit.");
        }

        listRepo.delete(list);
        return ResponseEntity.ok("Liste supprimée.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateList(@PathVariable Long id, @RequestBody ListEntity updatedList, Principal principal) {
        Optional<ListEntity> listOpt = listRepo.findById(id);
        if (listOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        
        ListEntity list = listOpt.get();
        if (!list.getUser().getUsername().equals(principal.getName())) {
            return ResponseEntity.status(403).body("Accès interdit.");
        }

        list.setName(updatedList.getName());
        ListEntity saved = listRepo.save(list);
        return ResponseEntity.ok(saved);
    }
}
