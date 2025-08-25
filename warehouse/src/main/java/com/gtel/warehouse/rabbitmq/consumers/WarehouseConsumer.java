package com.gtel.warehouse.rabbitmq.consumers;


import com.gtel.warehouse.models.request.HandleCreateOrderFailureEventMessage;
import com.gtel.warehouse.models.request.HandleEventOrderCreatedSuccessMessage;
import com.gtel.warehouse.models.request.HandleWarehouseSuccessEventMessage;
import com.gtel.warehouse.rabbitmq.producers.WarehouseProducer;
import com.gtel.warehouse.services.WarehouseService;
import com.nimbusds.jose.shaded.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
@Log4j2
public class WarehouseConsumer {
    private final WarehouseService warehouseService;
    private final WarehouseProducer warehouseProducer;

    @RabbitListener(queues = "${spring.rabbitmq.queues.order-created}")
    public void handleEventOrderCreatedSuccess(Message message) {
        String messageString = new String(message.getBody(), StandardCharsets.UTF_8);
        HandleEventOrderCreatedSuccessMessage body = new Gson().fromJson(messageString, HandleEventOrderCreatedSuccessMessage.class);
        try {
            warehouseService.handleWarehouseEvent(body);
            onWarehouseEventSuccess(body.getOrderId());
        } catch (Exception e) {
            onWarehouseEventFailure(body.getOrderId());
        }
    }
    private void onWarehouseEventSuccess(Long orderId) {
        warehouseProducer.sendWarehouseSuccessMessage(new HandleWarehouseSuccessEventMessage(orderId));
    }

    private void onWarehouseEventFailure(Long orderId) {
        warehouseProducer.sendWarehouseFailureMessage(new HandleCreateOrderFailureEventMessage(orderId));
    }
}
