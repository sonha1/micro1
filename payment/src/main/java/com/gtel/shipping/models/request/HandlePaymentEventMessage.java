package com.gtel.shipping.models.request;

import lombok.Data;

@Data
public class HandlePaymentEventMessage {
    private Long orderId;
}
