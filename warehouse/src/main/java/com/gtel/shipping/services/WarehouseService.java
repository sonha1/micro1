package com.gtel.shipping.services;


import com.gtel.order.grpc.OrderItemGrpc;
import com.gtel.shipping.entity.StockTransactionEnity;
import com.gtel.shipping.entity.WarehouseStockEntity;
import com.gtel.shipping.enums.StockTransactionAction;
import com.gtel.shipping.models.request.HandleEventOrderCreatedSuccessMessage;
import com.gtel.shipping.rabbitmq.producers.WarehouseProducer;
import com.gtel.shipping.repository.StockTransactionRepository;
import com.gtel.shipping.repository.WarehouseRepository;
import com.gtel.shipping.utils.ApplicationException;
import com.gtel.shipping.utils.ERROR_CODE;
import com.gtel.order.grpc.GetOrderDetailRequestGrpc;
import com.gtel.order.grpc.GetOrderDetailResponseGrpc;
import com.gtel.order.grpc.OrderGrpcServiceGrpc;
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

        // reserve stock + create transaction
        orderDetail.getItemsList().forEach(item -> {
            reserveStockWarehouse(item, orderDetail.getId());
        });
    }

    private void reserveStockWarehouse(OrderItemGrpc item, Long orderId) {
        Optional<WarehouseStockEntity> warehouseStockOptional = warehouseRepository.findByProductIdAndQuantityGreaterThanEqual(item.getProductId(), item.getQuantity());
        if (warehouseStockOptional.isEmpty()) {
            throw new ApplicationException(ERROR_CODE.RESOURCE_NOT_FOUND, "Product out of stock");
        }

        WarehouseStockEntity warehouseStock = warehouseStockOptional.get();
        warehouseStock.setQuantity(warehouseStock.getQuantity() - item.getQuantity());
        warehouseRepository.save(warehouseStock);

        StockTransactionEnity stockTransaction = new StockTransactionEnity();
        stockTransaction.setStockId(warehouseStock.getId());
        stockTransaction.setQuantity(item.getQuantity());
        stockTransaction.setNote("Buy product action");
        stockTransaction.setOrderId(orderId);
        stockTransaction.setAction(StockTransactionAction.EXPORT);
        stockTransactionRepository.save(stockTransaction);
    }
}
