package com.example.smartlock.security;

import com.example.smartlock.model.entity.CustomUserDetails;
import com.example.smartlock.repository.LockRepository;
import com.example.smartlock.repository.LockRoleRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component("lockGuard")
public class LockGuard {
    private final LockRoleRepository lockRoleRepository;
    private final LockRepository lockRepository;
    private final HttpServletRequest request;

    @Autowired
    public LockGuard(LockRoleRepository lockRoleRepository, LockRepository lockRepository, HttpServletRequest request) {
        this.lockRoleRepository = lockRoleRepository;
        this.lockRepository = lockRepository;
        this.request = request;
    }

    public boolean check(UUID lockId, String... allowedRoles) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UUID currentUserId = ((CustomUserDetails) auth.getPrincipal()).getId();

        List<String> allowedRoleList = Arrays.asList(allowedRoles);


        boolean debug = lockRoleRepository.existsByUserUserIdAndLockLockIdAndLockRoleIn(currentUserId, lockId, allowedRoleList);
        System.out.println("DEBUG: ID: " + currentUserId + " LOCK ID: " + lockId + " ALLOWED ROLES: " + allowedRoleList + " CHECK: " + debug);

        return debug;
    }

    public boolean checkLockSecret(UUID lockId) {
        String incomingSecret = request.getHeader("Device-Secret");
        System.out.println("DEBUG: LOCK ID: " + lockId + " SECRET: " + incomingSecret);
        if (incomingSecret == null || incomingSecret.isBlank()) return false;
        boolean debug = lockRepository.existsByLockIdAndSecretKey(lockId, incomingSecret);
        System.out.println("DEBUG: LOCK ID: " + lockId + " SECRET CHECK: " + debug);
        return debug;
    }
}
