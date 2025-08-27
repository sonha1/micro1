package com.gtel.order.models.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class GetOrderDetailResponse {
    private Long id;
    private BigDecimal totalPrice;
    private BigDecimal totalFee;
    private BigDecimal totalAmount;
    private Integer status;
    private String shippingMethod;
    private String address;
    private String phoneNumber;
    private String note;
    private Long userId;
}
