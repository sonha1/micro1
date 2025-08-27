package com.gtel.order.enums;


import lombok.Data;
import lombok.Getter;

@Getter
public enum OrderStatus {
    INIT(0),
    SUCCESS(1),
    FAILED(2),
    CANCEL(3);

    private final Integer value;

    OrderStatus(Integer value) {
        this.value = value;
    }
}
