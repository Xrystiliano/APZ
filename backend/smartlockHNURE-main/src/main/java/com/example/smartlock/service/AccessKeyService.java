package com.example.smartlock.service;

import com.example.smartlock.exceptions.exception.AccessKeyNotFoundException;
import com.example.smartlock.model.dto.accesskey.AccessKeyDto;
import com.example.smartlock.model.dto.accesskey.CreateKeyRequest;
import com.example.smartlock.model.entity.AccessKey;
import com.example.smartlock.model.entity.Lock;
import com.example.smartlock.repository.AccessKeyRepository;
import com.example.smartlock.security.LockGuard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
public class AccessKeyService {
    LockService lockService;
    UserService userService;
    LockGuard lockGuard;

    private final AccessKeyRepository accessKeyRepository;

    @Autowired
    public AccessKeyService(LockService lockService, UserService userService, AccessKeyRepository accessKeyRepository, LockGuard lockGuard) {
        this.lockService = lockService;
        this.userService = userService;
        this.accessKeyRepository = accessKeyRepository;
        this.lockGuard = lockGuard;
    }

    private AccessKeyDto fromAccessKeyToDto(AccessKey accessKey) {
        return new AccessKeyDto(
                accessKey.getAccessKeyId(),
                accessKey.getLock().getLockId(),
                accessKey.getToken(),
                accessKey.isActive(),
                accessKey.getValidFrom(),
                accessKey.getValidUntil()
        );
    }

    public AccessKeyDto createAccessKey(CreateKeyRequest request, UUID lockId, UUID userId) {
        byte[] randomBytes = new byte[32];
        new SecureRandom().nextBytes(randomBytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
        AccessKey accessKey = new AccessKey(
                lockService.getLockById(lockId),
                userService.getUserById(userId),
                token,
                request.getValidFrom(),
                request.getValidUntil(),
                true,
                OffsetDateTime.now()
        );
        return fromAccessKeyToDto(accessKeyRepository.save(accessKey));
    }

    public List<AccessKeyDto> getAllKeysOnLock(UUID lockId) {
        List<AccessKeyDto> accessKeyDtos = new ArrayList<>();
        List<AccessKey> accessKeys = accessKeyRepository.findAllByLock(lockService.getLockById(lockId));

        for (AccessKey accessKey : accessKeys) {
            accessKeyDtos.add(fromAccessKeyToDto(accessKey));
        }
        return accessKeyDtos;
    }

    public AccessKeyDto getAccessKeyById(UUID id) {
        return fromAccessKeyToDto(accessKeyRepository.findById(id).orElseThrow(()
                -> new AccessKeyNotFoundException("No access key with such id")));
    }

    public void deleteAccessKeyById(UUID id, UUID userId) {
        AccessKey accessKey = accessKeyRepository.findById(id).orElseThrow(() -> new AccessKeyNotFoundException("No access key with such id"));
        Lock lock = accessKey.getLock();
        if (lockGuard.check(lock.getLockId(), "OWNER", "ADMIN")) {
            accessKeyRepository.deleteById(id);
        }else {
            throw new AccessKeyNotFoundException("Permission denied");
        }

    }
}
