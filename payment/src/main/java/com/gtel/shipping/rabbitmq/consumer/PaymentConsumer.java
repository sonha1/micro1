package com.gtel.shipping.rabbitmq.consumer;


import com.gtel.shipping.models.request.HandleCreateOrderFailureEventMessage;
import com.gtel.shipping.models.request.HandlePaymentEventMessage;
import com.gtel.shipping.models.request.HandlePaymentEventSuccessMessage;
import com.gtel.shipping.rabbitmq.producer.PaymentProducer;
import com.gtel.shipping.service.WalletService;
import com.nimbusds.jose.shaded.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentConsumer {
    private final WalletService walletService;
    private final PaymentProducer paymentProducer;

    @RabbitListener(queues = "${spring.rabbitmq.queues.handle-warehouse-success}")
    public void handlePaymentEvent(Message message) {
        String messageString = new String(message.getBody(), StandardCharsets.UTF_8);
        HandlePaymentEventMessage body = new Gson().fromJson(messageString, HandlePaymentEventMessage.class);
        try {
            walletService.handlePaymentEvent(body);
            onPaymentEventSuccess(body.getOrderId());
        } catch (Exception e) {
            onPaymentEventFailure(body.getOrderId());
        }
    }

    private void onPaymentEventSuccess(Long orderId) {
        paymentProducer.sendPaymentSuccessMessage(new HandlePaymentEventSuccessMessage(orderId));
    }

    private void onPaymentEventFailure(Long orderId) {
        paymentProducer.sendPaymentFailureMessage(new HandleCreateOrderFailureEventMessage(orderId));
    }
}
