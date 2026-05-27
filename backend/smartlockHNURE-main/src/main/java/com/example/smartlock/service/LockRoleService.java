package com.example.smartlock.service;

import com.example.smartlock.model.dto.lock.LockDto;
import com.example.smartlock.model.dto.lockrole.EditLockRoleRequest;
import com.example.smartlock.model.dto.lockrole.LockRoleDto;
import com.example.smartlock.model.entity.Lock;
import com.example.smartlock.model.entity.LockRole;
import com.example.smartlock.model.entity.User;
import com.example.smartlock.model.enums.UserRole;
import com.example.smartlock.repository.LockRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class LockRoleService {
    private final LockRoleRepository lockRoleRepository;
    private final LockService lockService;
    private final UserService userService;

    @Autowired
    public LockRoleService(LockRoleRepository lockAccessRepository, LockService lockService, UserService userService) {
        this.lockRoleRepository = lockAccessRepository;
        this.lockService = lockService;
        this.userService = userService;
    }

    public LockRoleDto fromLockRoleToDto(LockRole lockrole) {
        return new LockRoleDto(
                lockrole.getUser().getEmail(),
                lockrole.getLock().getLockId(),
                lockrole.getUser().getUserId(),
                lockrole.getLockRole()
        );
    }

    public List<LockDto> getAllLocksByUserId(UUID userId) {
        List<Lock> locks = lockRoleRepository.findAllLockByUser(userService.getUserById(userId));
        List<LockDto> lockDtos = new ArrayList<>();

        for (Lock lock : locks) {
            lockDtos.add(lockService.fromLockToDto(lock));
        }

        return lockDtos;
    }

    public LockRoleDto addUserToLock(EditLockRoleRequest editLockRoleRequest, UUID lockId) {
        UUID userId = userService.getUserByEmail(editLockRoleRequest.getEmail()).orElseThrow().getUserId();

        return fromLockRoleToDto(
                lockRoleRepository.save(new LockRole(
                                userService.getUserById(userId),
                                lockService.getLockById(lockId),
                                editLockRoleRequest.getLockrole(),
                                OffsetDateTime.now()
                        )
                )
        );
    }

    public void addUserToLock(UserRole userRole, UUID userId, UUID lockId) {
        fromLockRoleToDto(
                lockRoleRepository.save(new LockRole(
                                userService.getUserById(userId),
                                lockService.getLockById(lockId),
                                userRole,
                                OffsetDateTime.now()
                        )
                )
        );
    }

    @Transactional
    public void deleteUserFromLock(String email, UUID lockId) {
        Lock lock = lockService.getLockById(lockId);
        User user = userService.getUserByEmail(email).orElseThrow();

        lockRoleRepository.deleteByUserAndLock(user, lock);
    }

    public LockRoleDto changeUserLockRole(EditLockRoleRequest editLockRoleRequest, UUID lockId) {
        Lock lock = lockService.getLockById(lockId);
        User user = userService.getUserByEmail(editLockRoleRequest.getEmail()).orElseThrow();

        LockRole lockRole = lockRoleRepository.findByLockAndUser(lock, user);
        lockRole.setLockRole(editLockRoleRequest.getLockrole());

        return fromLockRoleToDto(
                lockRoleRepository.save(lockRole)
        );
    }


    public List<LockRoleDto> getAllUsersOnLock(UUID lockId) {
        List<LockRoleDto> dtos = new ArrayList<>();
        List<LockRole> listLockRole = lockRoleRepository.findAllByLockLockId(lockId);

        for (LockRole lockRole : listLockRole) {
            dtos.add(fromLockRoleToDto(lockRole));
        }
        return dtos;
    }


}
