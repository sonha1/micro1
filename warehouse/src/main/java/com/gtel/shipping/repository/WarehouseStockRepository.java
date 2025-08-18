package com.gtel.shipping.repository;

import com.gtel.shipping.entity.WarehouseStockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseStockRepository extends JpaRepository<WarehouseStockEntity, Long> {
}
