package com.example.smartlock.repository;

import com.example.smartlock.model.entity.AccessKey;
import com.example.smartlock.model.entity.Lock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AccessKeyRepository extends JpaRepository<AccessKey, UUID> {
    List<AccessKey> findAllByLock(Lock lock);
}
