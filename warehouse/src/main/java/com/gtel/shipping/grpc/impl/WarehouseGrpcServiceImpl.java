package com.gtel.shipping.grpc.impl;


import com.google.protobuf.Empty;
import com.gtel.shipping.entity.StockTransactionEnity;
import com.gtel.shipping.entity.WarehouseStockEntity;
import com.gtel.shipping.enums.StockTransactionStatus;
import com.gtel.shipping.enums.StockTransactionAction;
import com.gtel.shipping.grpc.CreateWarehouseStockTransactionRequest;
import com.gtel.shipping.grpc.CreateWarehouseStockTransactionResponse;
import com.gtel.shipping.grpc.UpdateWarehouseStockTransactionRequest;
import com.gtel.shipping.grpc.WarehouseGrpcServiceGrpc;
import com.gtel.shipping.repository.StockTransactionRepository;
import com.gtel.shipping.repository.WarehouseRepository;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.grpc.server.service.GrpcService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@GrpcService
@RequiredArgsConstructor
@Slf4j
public class WarehouseGrpcServiceImpl extends WarehouseGrpcServiceGrpc.WarehouseGrpcServiceImplBase {
    private final WarehouseRepository warehouseRepository;
    private final StockTransactionRepository stockTransactionRepository;

    @Override
    public void updateStockTransaction(UpdateWarehouseStockTransactionRequest request, StreamObserver<Empty> responseObserver) {
        // get stock transaction + update
        Optional<StockTransactionEnity> stockTransactionOptional = stockTransactionRepository.findById(request.getStockTransactionId());
        if (stockTransactionOptional.isEmpty()) {
            responseObserver.onError(Status.NOT_FOUND.withDescription("Stock transaction is not found").asRuntimeException());
            return;
        }

        StockTransactionEnity stockTransaction = stockTransactionOptional.get();

        StockTransactionStatus status = request.getSuccess() ? StockTransactionStatus.SUCCESS : StockTransactionStatus.FAILED;
        stockTransactionRepository.save(stockTransaction);

        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    @Transactional
    public void createStockTransaction(CreateWarehouseStockTransactionRequest request, StreamObserver<CreateWarehouseStockTransactionResponse> responseObserver) {
        // lock stock for this product

        // get one ware house stock with stock > quantity
        Optional<WarehouseStockEntity> warehouseStockOptional = warehouseRepository.findByProductIdAndQuantityGreaterThanEqual(request.getProductId(), request.getQuantity());
        if (warehouseStockOptional.isEmpty()) {
            responseObserver.onError(Status.NOT_FOUND.withDescription("Product out of stock").asRuntimeException());
            return;
        }
        // update this stock
        WarehouseStockEntity warehouseStock = warehouseStockOptional.get();
        warehouseStock.setQuantity(warehouseStock.getQuantity() - request.getQuantity());
        warehouseRepository.save(warehouseStock);

        // create stock transaction
        StockTransactionEnity stockTransaction = new StockTransactionEnity();
        stockTransaction.setStockId(warehouseStock.getId());
        stockTransaction.setQuantity(request.getQuantity());
        stockTransaction.setNote("Buy product action");
        stockTransaction.setUserId(request.getUserId());
        stockTransaction.setAction(StockTransactionAction.EXPORT);
        stockTransactionRepository.save(stockTransaction);

        responseObserver.onNext(CreateWarehouseStockTransactionResponse.newBuilder().setStockTransactionId(stockTransaction.getId()).build());
        responseObserver.onCompleted();
    }
}
