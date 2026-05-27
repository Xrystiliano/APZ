package com.example.smartlock.model.dto.lockrole;

import com.example.smartlock.model.enums.UserRole;

public class EditLockRoleRequest {
    private UserRole lockrole;
    private String email;

    public EditLockRoleRequest(UserRole lockrole, String email) {
        this.lockrole = lockrole;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public UserRole getLockrole() {
        return lockrole;
    }
}
