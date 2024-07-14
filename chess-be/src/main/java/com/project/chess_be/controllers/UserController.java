package com.project.chess_be.controllers;

import com.project.chess_be.entities.User;
import com.project.chess_be.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController( UserService userService ){
        this.userService = userService;
    }

    @GetMapping("getUserId/{username}")
    public ResponseEntity<Integer> getUserId(@PathVariable String username) {
        Integer userId = userService.getUserId(username).get(0);
        return ResponseEntity.ok(userId);
    }

    @GetMapping("getUser/{userId}")
    public ResponseEntity<User> getUser(@PathVariable Integer userId) {
        Optional<User> userOptional = userService.getUser(userId);
        return userOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("saveUser")
    public ResponseEntity<User> postUser(@RequestBody User user) {
        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(savedUser);
    }
}
