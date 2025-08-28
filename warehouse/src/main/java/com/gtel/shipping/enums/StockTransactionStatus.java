package com.gtel.shipping.enums;

import lombok.Getter;

@Getter
public enum StockTransactionStatus {
    FAILED(0),
    SUCCESS(1),
    PENDING(2);

    private int value;

    StockTransactionStatus(int value) {
        this.value = value;
    }
}
