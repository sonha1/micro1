package com.gtel.shipping.rabbitmq.producers;


import com.gtel.shipping.models.request.HandleCreateOrderFailureEventMessage;
import com.gtel.shipping.models.request.HandleWarehouseSuccessEventMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class WarehouseProducer {
    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.exchanges.handle-warehouse-success}")
    private String successWarehouseExchange;

    @Value("${spring.rabbitmq.exchanges.create-order-failure}")
    private String failureWarehouseExchange;

    @Value("${spring.rabbitmq.routing-keys.handle-warehouse-success}")
    private String successWarehouseRoutingKey;

    @Autowired
    public WarehouseProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendHandleWarehouseSuccessMessage(HandleWarehouseSuccessEventMessage message) {
        rabbitTemplate.convertAndSend(successWarehouseExchange, successWarehouseRoutingKey, message);

    }

    public void sendHandleWarehouseFailureMessage(HandleCreateOrderFailureEventMessage message) {
        rabbitTemplate.convertAndSend(failureWarehouseExchange, "", message);
    }
}
