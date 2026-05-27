package com.example.smartlock.model.dto.lock;

import com.example.smartlock.model.enums.LockStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

public class LockDto {

    private UUID lockId;
    private String name;
    private LockStatus status;
    private OffsetDateTime lastHeartBeatAt;
    private boolean isLocked;

    public LockDto(UUID lockId, String name, LockStatus status, OffsetDateTime lastHeartBeatAt, boolean isLocked) {
        this.lockId = lockId;
        this.name = name;
        this.status = status;
        this.lastHeartBeatAt = lastHeartBeatAt;
        this.isLocked = isLocked;
    }

    public UUID getLockId() {
        return lockId;
    }

    public void setLockId(UUID lockId) {
        this.lockId = lockId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LockStatus getStatus() {
        return status;
    }

    public void setStatus(LockStatus status) {
        this.status = status;
    }

    public OffsetDateTime getLastHeartBeatAt() {
        return lastHeartBeatAt;
    }

    public void setLastHeartBeatAt(OffsetDateTime lastHeartBeatAt) {
        this.lastHeartBeatAt = lastHeartBeatAt;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }
}
