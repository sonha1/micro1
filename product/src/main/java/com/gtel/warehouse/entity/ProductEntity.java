package com.gtel.warehouse.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "product")
@Data
public class ProductEntity {

    @Id
    private long id;

    private String name;

    private int status;

    private BigDecimal price;
}
