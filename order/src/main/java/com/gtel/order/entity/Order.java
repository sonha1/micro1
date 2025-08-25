package com.gtel.order.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "total_fee")
    private BigDecimal totalFee;

    @Column(name = "total_tax")
    private BigDecimal totalTax;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "note")
    private String note;

    @Column(name = "user_id")
    private String userId;
}
