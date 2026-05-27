package com.example.smartlock.controller;

import com.example.smartlock.model.dto.lockrole.EditLockRoleRequest;
import com.example.smartlock.model.dto.lockrole.LockRoleDto;
import com.example.smartlock.model.enums.UserRole;
import com.example.smartlock.service.LockRoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/locks/{lockId}/roles")
public class LockRoleController {
    LockRoleService lockRoleService;

    public LockRoleController(LockRoleService lockRoleService) {
        this.lockRoleService = lockRoleService;
    }

    @PreAuthorize("@lockGuard.check(#lockId, 'ADMIN', 'OWNER')")
    @PostMapping
    public ResponseEntity<LockRoleDto> addUserToLock(
            @RequestBody EditLockRoleRequest editLockRoleRequest,
            @PathVariable UUID lockId) {

        LockRoleDto lockAccessDto = lockRoleService.addUserToLock(editLockRoleRequest, lockId);
        return ResponseEntity.ok(lockAccessDto);
    }

    @PreAuthorize("@lockGuard.check(#lockId, 'ADMIN', 'OWNER')")
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteUserFromLock( @PathVariable UUID lockId, @PathVariable String email) {
        lockRoleService.deleteUserFromLock(email, lockId);
        return ResponseEntity.ok(null);
    }

    @PreAuthorize("@lockGuard.check(#lockId, 'OWNER')")
    @PutMapping
    public ResponseEntity<LockRoleDto> editUserLockRole(
            @RequestBody EditLockRoleRequest editLockRoleRequest,
            @PathVariable UUID lockId) {


       LockRoleDto lockRoleDto = lockRoleService.changeUserLockRole(editLockRoleRequest, lockId);
        return ResponseEntity.ok(lockRoleDto);
    }

    @GetMapping
    public ResponseEntity<List<LockRoleDto>> findUsersOnLock(@PathVariable UUID lockId) {
        return ResponseEntity.ok(lockRoleService.getAllUsersOnLock(lockId));
    }
}
