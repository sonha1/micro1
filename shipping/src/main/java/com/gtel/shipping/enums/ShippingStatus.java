package com.gtel.shipping.enums;

import lombok.Getter;

@Getter
public enum ShippingStatus {
    INIT(0),
    PENDING(1),
    SHIPPED(2),
    DELIVERED(3),
    FAILED(4);
    private final int value;

    ShippingStatus(int value) {
        this.value = value;
    }
}
