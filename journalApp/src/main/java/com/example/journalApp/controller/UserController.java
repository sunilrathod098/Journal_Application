package com.example.journalApp.controller;

import com.example.journalApp.entity.UserModel;
import com.example.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import org.springframework.http.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping//here we fetch all user
    public List<UserModel> getAllUsers() {
        return userService.getAll();
    }


    @PostMapping //here we create a user
    public ResponseEntity<?> createUser(@RequestBody UserModel user) {
        try {
            userService.saveNewUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating user: " + e.getMessage());
        }
    }

    @PutMapping("/{username}")
    public ResponseEntity<?> updateUser(@PathVariable String username, @RequestBody UserModel user) {
        return userService.findByUsername(username)
                .map(existingUser -> {
                    existingUser.setUsername(user.getUsername());
                    existingUser.setPassword(user.getPassword());
                    existingUser.setEmail(user.getEmail());
                    userService.saveNewUser(existingUser);
                    return ResponseEntity.ok("User updated successfully");
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found"));
    }
}
