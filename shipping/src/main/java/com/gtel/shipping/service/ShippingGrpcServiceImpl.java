package com.gtel.shipping.service;

import com.google.protobuf.Empty;
import com.gtel.shipping.entity.ShippingEntity;
import com.gtel.shipping.enums.ShippingMethod;
import com.gtel.shipping.enums.ShippingStatus;
import com.gtel.shipping.grpc.CreateShippingRequest;
import com.gtel.shipping.grpc.CreateShippingResponse;
import com.gtel.shipping.grpc.ShippingGrpcServiceGrpc;
import com.gtel.shipping.grpc.UpdateShippingStatusRequest;
import com.gtel.shipping.repository.ShippingRepository;
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
