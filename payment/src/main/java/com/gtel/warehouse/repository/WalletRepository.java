package com.gtel.warehouse.repository;

import com.gtel.warehouse.entity.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<WalletEntity, Long> {
    Optional<WalletEntity> findByUserId(Long userId);
}
