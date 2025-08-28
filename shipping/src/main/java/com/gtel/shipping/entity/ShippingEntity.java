package com.gtel.shipping.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

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
    @Column(name = "method_id")
    private Long methodId;
    @Column(name = "expected_date")
    private LocalDateTime expectedDate;
    @Column(name = "shipping_date")
    private LocalDateTime shippingDate;
    @Column(name = "delivery_date")
    private LocalDateTime deliveryDate;
    @Column(name = "tracking_number")
    private String trackingNumber;
    @Column(name = "note")
    private String note;
    @Column(name = "phone_number")
    private String phoneNumber;
}
