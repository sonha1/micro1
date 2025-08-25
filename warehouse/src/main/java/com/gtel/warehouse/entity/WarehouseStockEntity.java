package com.gtel.warehouse.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "warehouse_stock")
@Getter
@Setter
public class WarehouseStockEntity extends BaseEntity {
    @Column(name = "product_id")
    private Long productId;
    @Column(name = "warehouse_id")
    private Long warehouseId;
    @Column(name = "quantity")
    private Integer quantity;
}
