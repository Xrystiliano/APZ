package com.example.smartlock.repository;

import com.example.smartlock.model.entity.Lock;
import com.example.smartlock.model.entity.LockRole;
import com.example.smartlock.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface LockRoleRepository extends JpaRepository <LockRole, UUID>{
    @Query("SELECT lr.lock FROM LockRole lr WHERE lr.user = :user")
    List<Lock> findAllLockByUser(User user);
    LockRole findByLockAndUser(Lock lock, User user);
    public void deleteByUserAndLock(User user, Lock lock);
    boolean existsByUserUserIdAndLockLockIdAndLockRoleIn(UUID userId, UUID lockId, Collection<String> LockRoles);

    List<LockRole> findAllByLockLockId(UUID id);
}
