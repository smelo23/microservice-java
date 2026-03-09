package com.usabana.microservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {

    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now());
        response.put("service", "microservice-java");
        response.put("version", "1.0.0");
        return response;
    }

    @GetMapping("/hello")
    public Map<String, String> hello() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Hola desde el microservicio!");
        response.put("author", "Sebastian Melo");
        return response;
    }

    @GetMapping("/hello/{name}")
    public Map<String, String> helloName(@PathVariable String name) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Hola " + name + "!");
        response.put("timestamp", LocalDateTime.now().toString());
        return response;
    }
}
