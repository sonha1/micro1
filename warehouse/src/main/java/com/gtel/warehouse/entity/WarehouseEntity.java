package com.gtel.warehouse.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "warehouses")
@Getter
@Setter
public class WarehouseEntity extends BaseEntity {
    private String name;
    private String address;
    private String code;
}
