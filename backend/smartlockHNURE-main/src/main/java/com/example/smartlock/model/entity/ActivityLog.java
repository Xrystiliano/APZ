package com.example.smartlock.model.entity;

import com.example.smartlock.model.enums.ActionType;
import jakarta.persistence.*;

import java.math.BigInteger;
import java.time.OffsetDateTime;

@Entity
@Table(name = "activitylogs")
public class ActivityLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long logId;

    @ManyToOne
    @JoinColumn(name = "lock_id")
    private Lock actorLock;

    @ManyToOne
    @JoinColumn(name="actor_user_id")
    private User actorUser;

    @ManyToOne
    @JoinColumn(name="actor_key_id")
    private AccessKey accessKey;

    @Column(name="action_type", nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private ActionType actionType;


    @Column(name = "timestamp", nullable = false, updatable = false)
    private OffsetDateTime timestamp;

    @Column(name = "details", updatable = false)
    private String details;

    protected ActivityLog() {}

    public ActivityLog(Long logId, Lock lock, User userId, AccessKey accessKey, OffsetDateTime timestamp, String details, ActionType actionType) {
        this.logId = logId;
        this.actorLock = lock;
        this.actorUser = userId;
        this.accessKey = accessKey;
        this.timestamp = timestamp;
        this.details = details;
        this.actionType = actionType;
    }

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public Lock getActorLock() {
        return actorLock;
    }

    public void setActorLock(Lock actorLock) {
        this.actorLock = actorLock;
    }

    public User getActorUser() {
        return actorUser;
    }

    public void setActorUser(User actorUser) {
        this.actorUser = actorUser;
    }

    public AccessKey getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(AccessKey accessKey) {
        this.accessKey = accessKey;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }
}
