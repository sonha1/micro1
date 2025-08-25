package com.gtel.warehouse.models.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HandleWarehouseSuccessEventMessage {
    private Long orderId;
}
