package com.example.smartlock.model.entity;


import com.example.smartlock.model.enums.LockStatus;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "locks")
public class Lock {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "lock_id", nullable = false, updatable = false)
    private UUID lockId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "lock")
    private Set<AccessKey> accessKeys;

    @OneToMany(mappedBy = "actorLock")
    private Set<ActivityLog> activityLogs;

    @OneToMany(mappedBy = "lock")
    private Set<LockRole> lockAccesses;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "device_serial_number", nullable = false, updatable = false, unique = true)
    private String serialNumber;

    @Column(name = "timezone", nullable = false)
    private String timezone;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private LockStatus status;

    @Column(name = "is_locked", nullable = false)
    private boolean isLocked;

    @Column(name = "last_heartbeat_at", nullable = false)
    private OffsetDateTime lastHeartbeatAt;

    @Column(name="created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name="secret_key", nullable = false, updatable = false)
    private String secretKey;

    protected Lock() {
    }

    public Lock(User user, String name, String serialNumber, String timezone, LockStatus status, boolean isLocked, OffsetDateTime lastHeartbeatAt, OffsetDateTime createdAt, String secretKey) {
        this.user = user;
        this.name = name;
        this.serialNumber = serialNumber;
        this.timezone = timezone;
        this.status = status;
        this.isLocked = isLocked;
        this.lastHeartbeatAt = lastHeartbeatAt;
        this.createdAt = createdAt;
        this.secretKey = secretKey;
    }

    public UUID getLockId() {
        return lockId;
    }

    public void setLockId(UUID lockId) {
        this.lockId = lockId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<AccessKey> getAccessKeys() {
        return accessKeys;
    }

    public void setAccessKeys(Set<AccessKey> accessKeys) {
        this.accessKeys = accessKeys;
    }

    public Set<ActivityLog> getActivityLogs() {
        return activityLogs;
    }

    public void setActivityLogs(Set<ActivityLog> activityLogs) {
        this.activityLogs = activityLogs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public LockStatus getStatus() {
        return status;
    }

    public void setStatus(LockStatus status) {
        this.status = status;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public OffsetDateTime getLastHeartbeatAt() {
        return lastHeartbeatAt;
    }

    public void setLastHeartbeatAt(OffsetDateTime lastHeartbeatAt) {
        this.lastHeartbeatAt = lastHeartbeatAt;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Set<LockRole> getLockAccesses() {
        return lockAccesses;
    }

    public void setLockAccesses(Set<LockRole> lockAccesses) {
        this.lockAccesses = lockAccesses;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        secretKey = secretKey;
    }
}
