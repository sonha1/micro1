package com.gtel.shipping.services;


import com.gtel.shipping.models.request.HandleEventOrderCreatedSuccessMessage;
import com.gtel.shipping.repository.WarehouseRepository;
import com.nimbusds.jose.shaded.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
public class WarehouseService {
    private final WarehouseRepository warehouseRepository;

    @Transactional
    public void handlerWarehouseEventSuccess(HandleEventOrderCreatedSuccessMessage message) {
        // get order
        // check stock warehouse
        // update stock (reserve quantity)
        // create stock transaction
        // publish message to payment
    }

    @Transactional
    public void handlerWarehouseEventFailure(HandleEventOrderCreatedSuccessMessage message) {
        // log
        // pushlish messsage
    }
}
