package com.gtel.order.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
    @Column(name = "total_fee")
    private BigDecimal totalFee;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "shipping_method")
    private Integer ShippingMethod;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "note")
    private String note;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "status")
    private Integer status;

}
