package com.example.smartlock.model.entity;

import com.example.smartlock.model.enums.UserRole;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "lock_roles",uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "lock_id"}
        )})
public class LockRole {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="access_id")
    private UUID accessId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "lock_id")
    private Lock lock;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole lockRole;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    protected LockRole(){}

    public LockRole(User user, Lock lock, UserRole lockRole, OffsetDateTime createdAt) {
        this.user = user;
        this.lock = lock;
        this.lockRole = lockRole;
        this.createdAt = createdAt;
    }

    public UUID getAccessId() {
        return accessId;
    }

    public void setAccessId(UUID accessId) {
        this.accessId = accessId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Lock getLock() {
        return lock;
    }

    public void setLock(Lock lock) {
        this.lock = lock;
    }

    public UserRole getLockRole() {
        return lockRole;
    }

    public void setLockRole(UserRole lockRole) {
        this.lockRole = lockRole;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
