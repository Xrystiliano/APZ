package com.example.smartlock.repository;

import com.example.smartlock.model.entity.Lock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LockRepository extends JpaRepository<Lock, UUID> {
    boolean existsByLockIdAndSecretKey(UUID lockId, String secretKey);
}
