package com.gtel.warehouse.repository;

import com.gtel.warehouse.entity.WarehouseStockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseRepository extends JpaRepository<WarehouseStockEntity, Long> {
}
