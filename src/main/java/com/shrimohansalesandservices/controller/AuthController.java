package com.shrimohansalesandservices.controller;

import com.shrimohansalesandservices.config.JwtUtil;
import com.shrimohansalesandservices.dto.AuthResponse;
import com.shrimohansalesandservices.dto.LoginRequest;
import com.shrimohansalesandservices.dto.RegisterRequest;
import com.shrimohansalesandservices.entity.Role;
import com.shrimohansalesandservices.entity.User;
import com.shrimohansalesandservices.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Register
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request) {

        // Email already exist karta hai?

        if (userService.existsByEmail(
                request.getEmail())) {
            return ResponseEntity.badRequest()
                    .body("Email already exists!");
        }

        // User banao

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(
                        request.getPassword()))
                .phone(request.getPhone())
                .address(request.getAddress())
                .role(Role.USER)
                .build();

        userService.save(user);

        return ResponseEntity.ok("Registered Successfully!");
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request) {
        try {
            // Authenticate karo

            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()));

            // User find karo

            User user = userService
                    .findByEmail(request.getEmail())
                    .orElseThrow();

            // Token generate karo

            String token = jwtUtil.generateToken(
                    user.getEmail(),
                    user.getRole().name());

            // Response bhejo

            return ResponseEntity.ok(
                    new AuthResponse(
                            token,
                            user.getName(),
                            user.getEmail(),
                            user.getRole().name()));

        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest()
                    .body("Invalid email or password!");
        }
    }
}