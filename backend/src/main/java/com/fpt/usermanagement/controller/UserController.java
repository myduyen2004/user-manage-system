package com.fpt.usermanagement.controller;

import com.fpt.usermanagement.dto.*;
import com.fpt.usermanagement.entity.User;
import com.fpt.usermanagement.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class UserController {
    
    private final UserService userService;
    
    // Get all users (ADMIN only)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getAllUsers() {
        try {
            List<UserResponse> users = userService.getAllUsers();
            return ResponseEntity.ok(new ApiResponse(true, "Users retrieved successfully", users));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Failed to retrieve users: " + e.getMessage()));
        }
    }
    
    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long id) {
        try {
            UserResponse user = userService.getUserById(id);
            return ResponseEntity.ok(new ApiResponse(true, "User retrieved successfully", user));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Failed to retrieve user: " + e.getMessage()));
        }
    }
    
    // Get users by role (ADMIN only)
    @GetMapping("/role/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getUsersByRole(@PathVariable String role) {
        try {
            User.Role userRole = User.Role.valueOf(role.toUpperCase());
            List<UserResponse> users = userService.getUsersByRole(userRole);
            return ResponseEntity.ok(new ApiResponse(true, "Users retrieved successfully", users));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Failed to retrieve users: " + e.getMessage()));
        }
    }
    
    // Update user
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody RegisterRequest request) {
        try {
            ApiResponse response = userService.updateUser(id, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Failed to update user: " + e.getMessage()));
        }
    }
    
    // Delete user (ADMIN only)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long id) {
        try {
            ApiResponse response = userService.deleteUser(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Failed to delete user: " + e.getMessage()));
        }
    }
    
    // Toggle user status (ADMIN only)
    @PatchMapping("/{id}/toggle-status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> toggleUserStatus(@PathVariable Long id) {
        try {
            ApiResponse response = userService.toggleUserStatus(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Failed to toggle user status: " + e.getMessage()));
        }
    }
}
