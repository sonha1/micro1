package com.gtel.warehouse.services;


import com.gtel.warehouse.entity.StockTransactionEnity;
import com.gtel.warehouse.entity.WarehouseStockEntity;
import com.gtel.warehouse.enums.StockTransactionAction;
import com.gtel.warehouse.models.request.HandleCreateOrderFailureEventMessage;
import com.gtel.warehouse.models.request.HandleEventOrderCreatedSuccessMessage;
import com.gtel.warehouse.models.request.HandleWarehouseSuccessEventMessage;
import com.gtel.warehouse.rabbitmq.producers.WarehouseProducer;
import com.gtel.warehouse.repository.StockTransactionRepository;
import com.gtel.warehouse.repository.WarehouseRepository;
import com.gtel.warehouse.utils.ApplicationException;
import com.gtel.warehouse.utils.ERROR_CODE;
import com.gtel.warehouse.grpc.GetOrderDetailRequestGrpc;
import com.gtel.warehouse.grpc.GetOrderDetailResponseGrpc;
import com.gtel.warehouse.grpc.OrderGrpcServiceGrpc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class WarehouseService {
    private final WarehouseRepository warehouseRepository;
    private final StockTransactionRepository stockTransactionRepository;
    private final WarehouseProducer warehouseProducer;

    @GrpcClient("order-service")
    private OrderGrpcServiceGrpc.OrderGrpcServiceBlockingStub orderServiceBlockingStub;

    @Transactional
    public void handleWarehouseEvent(HandleEventOrderCreatedSuccessMessage message) {
        // get order
        GetOrderDetailResponseGrpc orderDetail = orderServiceBlockingStub.getOrderDetail(GetOrderDetailRequestGrpc.newBuilder().setOrderId(message.getOrderId()).build());
        // check stock warehouse
        Optional<WarehouseStockEntity> warehouseStockOptional = warehouseRepository.findByProductIdAndQuantityGreaterThanEqual(orderDetail.getProductId(), orderDetail.getQuantity());
        if (warehouseStockOptional.isEmpty()) {
            throw new ApplicationException(ERROR_CODE.RESOURCE_NOT_FOUND, "Product out of stock");
        }

        // update stock (reserve quantity)
        WarehouseStockEntity warehouseStock = warehouseStockOptional.get();
        warehouseStock.setQuantity(warehouseStock.getQuantity() - orderDetail.getQuantity());
        warehouseRepository.save(warehouseStock);

        // create stock transaction
        StockTransactionEnity stockTransaction = new StockTransactionEnity();
        stockTransaction.setStockId(warehouseStock.getId());
        stockTransaction.setQuantity(orderDetail.getQuantity());
        stockTransaction.setNote("Buy product action");
        stockTransaction.setUserId(orderDetail.getUserId());
        stockTransaction.setAction(StockTransactionAction.EXPORT);
        stockTransactionRepository.save(stockTransaction);
    }


}
