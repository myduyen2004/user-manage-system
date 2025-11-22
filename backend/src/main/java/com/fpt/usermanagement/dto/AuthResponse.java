package com.fpt.usermanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String role;
    
    public AuthResponse(String token, String type, Long id, String username, 
                       String email, String fullName, String role) {
        this.token = token;
        this.type = type;
        this.id = id;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
    }
}
