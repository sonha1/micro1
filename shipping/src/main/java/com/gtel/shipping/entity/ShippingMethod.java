package com.gtel.shipping.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "shipping_methods")
public class ShippingMethod extends BaseEntity {
    @Column(name = "fee")
    private BigDecimal fee;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "provider")
    private String provider;

    @Column(name = "provider")
    private Boolean isActive;
}
