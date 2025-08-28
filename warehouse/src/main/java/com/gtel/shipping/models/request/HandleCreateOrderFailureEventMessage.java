package com.gtel.shipping.models.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HandleCreateOrderFailureEventMessage {
    private Long orderId;
}
