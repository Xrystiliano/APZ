package com.example.smartlock.repository;

import com.example.smartlock.model.entity.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, BigInteger> {
}
