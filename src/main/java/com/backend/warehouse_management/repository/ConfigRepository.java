package com.backend.warehouse_management.repository;

import com.backend.warehouse_management.entity.AdminConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfigRepository extends JpaRepository<AdminConfig, Long> {

    Optional<AdminConfig> findByConfigName(String configName);

    boolean existsByConfigName(String configName);
}
