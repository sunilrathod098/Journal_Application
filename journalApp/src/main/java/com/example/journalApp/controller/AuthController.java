package com.example.journalApp.controller;


import com.example.journalApp.entity.UserModel;
import com.example.journalApp.service.UserService;
import com.example.journalApp.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    //Register user (role is "USER")
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserModel user) {
        try {
            userService.saveNewUser(user);
            //System.out.println("Password received = " + user.getPassword());
            return ResponseEntity.status(201).body("User Registered Successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }


    //here we logged in the user through return JWT token
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");

        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

        //if no exception, authentication succeeded
        UserModel user = userService.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        String token = jwtUtil.generateToken(user.getEmail(), user.getRoles().isEmpty() ? "USER" : user.getRoles().get(0));
        return ResponseEntity.ok(Map.of("Token", token));
    }
}
