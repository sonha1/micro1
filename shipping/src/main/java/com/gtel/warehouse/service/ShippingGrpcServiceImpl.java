package com.gtel.warehouse.service;

import com.google.protobuf.Empty;
import com.gtel.warehouse.entity.ShippingEntity;
import com.gtel.warehouse.enums.ShippingMethod;
import com.gtel.warehouse.enums.ShippingStatus;
import com.gtel.warehouse.grpc.CreateShippingRequest;
import com.gtel.warehouse.grpc.CreateShippingResponse;
import com.gtel.warehouse.grpc.ShippingGrpcServiceGrpc;
import com.gtel.warehouse.grpc.UpdateShippingStatusRequest;
import com.gtel.warehouse.repository.ShippingRepository;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.grpc.server.service.GrpcService;

import java.math.BigDecimal;

@GrpcService
@RequiredArgsConstructor
@Slf4j
public class ShippingGrpcServiceImpl extends ShippingGrpcServiceGrpc.ShippingGrpcServiceImplBase {

    private final ShippingRepository shippingRepository;

    @Override
    public void createShipping(CreateShippingRequest request, StreamObserver<CreateShippingResponse> responseObserver) {
        ShippingEntity shipping = new ShippingEntity();
        shipping.setOrderId(request.getOrderId());
        shipping.setAddress(request.getAddress());
        shipping.setStatus(ShippingStatus.INIT.getValue());
        shipping.setMethod(ShippingMethod.valueOf(request.getMethod()).name());
        shipping.setFee(new BigDecimal(request.getFee()));
        shipping.setNote(request.getNote());
        shippingRepository.save(shipping);

        responseObserver.onNext(CreateShippingResponse.newBuilder().setShippingId(shipping.getId()).build());
        responseObserver.onCompleted();
    }

    @Override
    public void updateShippingStatus(UpdateShippingStatusRequest request, StreamObserver<Empty> responseObserver) {
        super.updateShippingStatus(request, responseObserver);
    }
}
