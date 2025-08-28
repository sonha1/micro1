package com.gtel.shipping.models.request;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HandlePaymentEventSuccessMessage {
    private Long orderId;
}
