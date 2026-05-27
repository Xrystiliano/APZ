package com.example.smartlock.model.entity;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "accesskeys")
public class AccessKey {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "access_key_id", nullable = false, updatable = false)
    private UUID accessKeyId;

    @ManyToOne
    @JoinColumn(name = "lock_id")
    private Lock lock;

    @ManyToOne
    @JoinColumn(name = "created_by_user_id")
    private User user;

    @Column(name = "access_token", updatable = false, unique = true, nullable = false)
    private String token;

    @Column(name="valid_from", nullable = false, updatable = false)
    private OffsetDateTime validFrom;

    @Column(name="valid_until", nullable = false, updatable = false)
    private OffsetDateTime validUntil;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Column(name="created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    protected AccessKey(){}

    public AccessKey(Lock lock, User user, String token, OffsetDateTime validFrom, OffsetDateTime validUntil, boolean isActive, OffsetDateTime createdAt) {
        this.lock = lock;
        this.user = user;
        this.token = token;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    public UUID getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(UUID accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public Lock getLock() {
        return lock;
    }

    public void setLock(Lock lock) {
        this.lock = lock;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public OffsetDateTime getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(OffsetDateTime validFrom) {
        this.validFrom = validFrom;
    }

    public OffsetDateTime getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(OffsetDateTime validUntil) {
        this.validUntil = validUntil;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
