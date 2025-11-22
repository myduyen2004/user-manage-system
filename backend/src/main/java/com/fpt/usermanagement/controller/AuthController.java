package com.fpt.usermanagement.controller;

import com.fpt.usermanagement.dto.*;
import com.fpt.usermanagement.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class AuthController {
    
    private final UserService userService;
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            ApiResponse response = userService.register(request);
            if (response.getSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Registration failed: " + e.getMessage()));
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            AuthResponse response = userService.login(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Login failed: " + e.getMessage()));
        }
    }
}
