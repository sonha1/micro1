package com.gtel.shipping.rabbitmq.producer;

import com.gtel.shipping.models.request.HandleCreateOrderFailureEventMessage;
import com.gtel.shipping.models.request.HandlePaymentEventSuccessMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class PaymentProducer {
    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.exchanges.handle-payment-success}")
    private String successPaymentExchange;

    @Value("${spring.rabbitmq.exchanges.create-order-failure}")
    private String failurePaymentExchange;

    @Value("${spring.rabbitmq.routing-keys.handle-payment-success}")
    private String successPaymentRoutingKey;

    @Autowired
    public PaymentProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendPaymentSuccessMessage(HandlePaymentEventSuccessMessage message) {
        rabbitTemplate.convertAndSend(successPaymentExchange, successPaymentRoutingKey, message);
    }

    public void sendPaymentFailureMessage(HandleCreateOrderFailureEventMessage message) {
        rabbitTemplate.convertAndSend(failurePaymentExchange, "", message);
    }
}

