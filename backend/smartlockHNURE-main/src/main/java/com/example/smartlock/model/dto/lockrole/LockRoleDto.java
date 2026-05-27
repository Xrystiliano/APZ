package com.example.smartlock.model.dto.lockrole;

import com.example.smartlock.model.enums.UserRole;

import java.util.UUID;

public class LockRoleDto {
    private UUID lockID;
    private UUID userID;
    private UserRole lockRole;
    private final String email;

    public LockRoleDto( String email, UUID lockID, UUID userID, UserRole lockRole) {
        this.lockID = lockID;
        this.userID = userID;
        this.lockRole = lockRole;
        this.email = email;
    }

    public UUID getLockID() {
        return lockID;
    }

    public void setLockID(UUID lockID) {
        this.lockID = lockID;
    }

    public UUID getUserID() {
        return userID;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
    }

    public UserRole getLockRole() {
        return lockRole;
    }

    public void setLockRole(UserRole lockRole) {
        this.lockRole = lockRole;
    }

    public String getEmail() {
        return email;
    }
}
