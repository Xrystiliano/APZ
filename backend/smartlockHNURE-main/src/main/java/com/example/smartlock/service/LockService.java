package com.example.smartlock.service;

import com.example.smartlock.exceptions.exception.LockNotFoundException;
import com.example.smartlock.model.dto.lock.CreateLockRequest;
import com.example.smartlock.model.dto.lock.EditLockRequest;
import com.example.smartlock.model.dto.lock.LockDto;
import com.example.smartlock.model.entity.Lock;
import com.example.smartlock.model.enums.LockStatus;
import com.example.smartlock.repository.LockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.security.SecureRandom;

@Service
public class LockService {
    LockRepository lockRepository;
    UserService userService;

    public LockService(LockRepository lockRepository, UserService userService) {
        this.lockRepository = lockRepository;
        this.userService = userService;
    }

    public LockDto fromLockToDto(Lock lock) {
        return new LockDto(
                lock.getLockId(),
                lock.getName(),
                lock.getStatus(),
                lock.getLastHeartbeatAt(),
                lock.isLocked()
        );
    };

    public static String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }

        return sb.toString();
    }

    public Lock getLockById(UUID lockId) {
        return lockRepository.findById(lockId)
                .orElseThrow(()-> new LockNotFoundException("No lock with such id"));
    }

    public LockDto createLock(CreateLockRequest request, UUID userId){
        Lock lock = new Lock (
                userService.getUserById(userId),
                request.getName(),
                request.getSerialNumber(),
                request.getTimezone(),
                LockStatus.ONLINE,
                false,
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                generateRandomString(32)
        );

        return fromLockToDto(
                lockRepository.save(lock)
        );

    }

    @Transactional
    public void deleteLock(UUID id){
        lockRepository.deleteById(id);
    }

    public LockDto editLock(EditLockRequest request, UUID lockId){
        Lock lock = lockRepository.findById(lockId).get();
        lock.setName(request.getName());

        return fromLockToDto(
                lockRepository.save(lock)
        );
    }


    public LockDto lockLock(UUID lockId) {
        Lock lock = lockRepository.findById(lockId).get();
        lock.setLocked(true);
        return fromLockToDto(
                lockRepository.save(lock)
        );
    }

    public LockDto unlockLock(UUID lockId) {
        Lock lock = lockRepository.findById(lockId).get();
        lock.setLocked(false);
        return fromLockToDto(
                lockRepository.save(lock)
        );
    }

    public Void updateHeartbeat(UUID lockId) {
        Lock lock = lockRepository.findById(lockId).get();
        lock.setLastHeartbeatAt(OffsetDateTime.now());
        lockRepository.save(lock);
        return null;
    }


    public boolean isLockedById(UUID lockId) {
        Lock lock = lockRepository.findById(lockId).orElseThrow(()-> new LockNotFoundException("No lock with such id"));
        return lock.isLocked();
    }
}
