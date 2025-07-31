package com.todo.backend.controller;

import com.todo.backend.service.WitAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.json.JSONObject;

@RestController
@RequestMapping("/api/chatbot")
@CrossOrigin
public class ChatBotController {

    @Autowired
    private WitAiService witAiService;


    @PostMapping
    public ResponseEntity<String> handleMessage(@RequestBody String message) {
        JSONObject response = witAiService.analyzeMessage(message);
        String datetime = witAiService.extractDatetime(response);

        System.out.println("⏰ Date extraite : " + datetime);

        // Tu peux créer une tâche ici si datetime != null
        // Exemple (pseudo-code, adapte-le à ton TaskController/service) :
        if (datetime != null) {
            // Crée un objet tâche (ex: Task task = new Task("Nouvelle tâche", datetime, ...))
            // taskService.save(task);
        }

        return ResponseEntity.ok(response.toString(2)); // jolis retours JSON
    }
}
