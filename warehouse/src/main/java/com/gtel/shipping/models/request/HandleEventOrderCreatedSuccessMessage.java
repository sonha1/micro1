package com.gtel.shipping.models.request;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HandleEventOrderCreatedSuccessMessage {
    private Long orderId;
}
