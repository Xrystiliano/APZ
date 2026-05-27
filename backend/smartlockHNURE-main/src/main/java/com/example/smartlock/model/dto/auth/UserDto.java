package com.example.smartlock.model.dto.auth;

import java.util.UUID;

public class UserDto {
    private UUID id;
    private String email;
    private String fullName;

    protected UserDto() {}

    public UserDto(UUID id, String email, String fullName) {
        this.email = email;
        this.fullName = fullName;
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
