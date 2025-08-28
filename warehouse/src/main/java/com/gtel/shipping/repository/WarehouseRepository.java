package com.gtel.shipping.repository;

import com.gtel.shipping.entity.WarehouseStockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WarehouseRepository extends JpaRepository<WarehouseStockEntity, Long> {

    Optional<WarehouseStockEntity> findByProductIdAndQuantityGreaterThanEqual(Long productId, Integer quantity);
}
