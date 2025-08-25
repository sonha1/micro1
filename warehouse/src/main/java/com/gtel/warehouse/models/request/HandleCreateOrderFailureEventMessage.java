package com.gtel.warehouse.models.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HandleCreateOrderFailureEventMessage {
    private Long orderId;
}
