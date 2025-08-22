package com.gtel.shipping.models.request;

import lombok.Data;

@Data
public class HandleCreateOrderFailureEventMessage {
    private Long orderId;
}
