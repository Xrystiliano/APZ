package com.example.smartlock.controller;

import com.example.smartlock.model.dto.accesskey.AccessKeyDto;
import com.example.smartlock.model.dto.accesskey.CreateKeyRequest;
import com.example.smartlock.model.entity.CustomUserDetails;
import com.example.smartlock.service.AccessKeyService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/access-key")
public class AccessKeyController {

    private final AccessKeyService accessKeyService;

    public AccessKeyController(AccessKeyService accessKeyService) {
        this.accessKeyService = accessKeyService;
    }

    @PreAuthorize("@lockGuard.check(#lockId, 'MEMBER', 'ADMIN', 'OWNER')")
    @PostMapping("/lock/{lockId}")
    public ResponseEntity<AccessKeyDto> createAccessKey(@PathVariable UUID lockId, @RequestBody CreateKeyRequest createKeyRequest, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UUID userId = userDetails.getId();

        AccessKeyDto accessKeyDto = accessKeyService.createAccessKey(createKeyRequest, lockId, userId);
        return ResponseEntity.ok(accessKeyDto);
    }

    @PreAuthorize("@lockGuard.check(#lockId, 'ADMIN', 'OWNER')")
    @GetMapping("/lock/{lockId}")
    public ResponseEntity<List<AccessKeyDto>> getAllKeysOnLock(@PathVariable UUID lockId, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UUID userId = userDetails.getId();

        List<AccessKeyDto> accessKeyDtos = accessKeyService.getAllKeysOnLock(lockId);
        return ResponseEntity.ok(accessKeyDtos);
    }

    @GetMapping("/{keyId}")
    public ResponseEntity<AccessKeyDto> getKeyById(@PathVariable UUID keyId) {
        AccessKeyDto accessKeyDto = accessKeyService.getAccessKeyById(keyId);
        return ResponseEntity.ok(accessKeyDto);
    }

    @DeleteMapping("/{keyId}")
    public ResponseEntity<AccessKeyDto> deleteKeyById(@PathVariable UUID keyId, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UUID userId = userDetails.getId();

        accessKeyService.deleteAccessKeyById(keyId, userId);
        return ResponseEntity.ok(null);
    }
}
