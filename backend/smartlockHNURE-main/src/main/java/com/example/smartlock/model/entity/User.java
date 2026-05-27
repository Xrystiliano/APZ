package com.example.smartlock.model.entity;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "user_id", unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name="full_name", nullable = false)
    private String fullName;

    @Column(name="created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @OneToMany(mappedBy = "user")
    private Set<Lock> locks;

    @OneToMany(mappedBy = "user")
    private Set<AccessKey> accessKeys;

    @OneToMany(mappedBy = "actorUser")
    private Set<ActivityLog> activityLogs;

    @OneToMany(mappedBy = "user")
    private Set<LockRole> lockAccesses;

    protected User() {}

    public User(String email, String passwordHash, String fullName, OffsetDateTime createdAt) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.createdAt = createdAt;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Set<Lock> getLocks() {
        return locks;
    }

    public void setLocks(Set<Lock> locks) {
        this.locks = locks;
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

    public Set<LockRole> getLockAccesses() {
        return lockAccesses;
    }

    public void setLockAccesses(Set<LockRole> lockAccesses) {
        this.lockAccesses = lockAccesses;
    }
}
