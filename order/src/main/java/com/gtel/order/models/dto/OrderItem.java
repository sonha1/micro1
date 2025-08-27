package com.gtel.order.models.dto;

import lombok.Data;

@Data
public class OrderItem {
    private long productId;
    private int quantity;
}
