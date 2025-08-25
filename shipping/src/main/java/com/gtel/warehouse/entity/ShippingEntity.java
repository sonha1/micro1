package com.gtel.warehouse.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "shipping")
public class ShippingEntity extends BaseEntity {
    @Column(name = "order_id")
    private Long orderId;
    @Column(name = "address")
    private String address;
    @Column(name = "status")
    private Integer status;
    @Column(name = "method")
    private String method;
    @Column(name = "shipping_date")
    private LocalDateTime shippingDate;
    @Column(name = "delivery_date")
    private LocalDateTime deliveryDate;
    @Column(name = "fee")
    private BigDecimal fee;
    @Column(name = "tracking_number")
    private String trackingNumber;
    @Column(name = "note")
    private String note;
}
