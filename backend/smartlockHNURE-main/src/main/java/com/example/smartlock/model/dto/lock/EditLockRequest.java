package com.example.smartlock.model.dto.lock;

import java.util.UUID;

public class EditLockRequest {
    private String name;

    public EditLockRequest(UUID lockID, String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
