package com.example.smartlock.model.dto.auth;

import java.util.UUID;

public class AuthResponse {
    private UUID id;
    private String jwtToken;
    private String email;
    private String fullName;

    public AuthResponse(UUID id, String jwtToken, String email, String fullName) {
        this.id = id;
        this.jwtToken = jwtToken;
        this.email = email;
        this.fullName = fullName;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
