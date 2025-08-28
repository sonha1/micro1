package com.gtel.shipping.service.grpc;


import com.gtel.shipping.entity.ShippingMethod;
import com.gtel.shipping.grpc.ShippingGrpcServiceGrpc;
import com.gtel.shipping.grpc.ValidateShippingRequest;
import com.gtel.shipping.grpc.ValidateShippingResponse;
import com.gtel.shipping.repository.ShippingMethodRepository;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShippingServiceGrpcServiceImpl extends ShippingGrpcServiceGrpc.ShippingGrpcServiceImplBase {
    private final ShippingMethodRepository shippingMethodRepository;

    @Override
    public void validateShipping(ValidateShippingRequest request, StreamObserver<ValidateShippingResponse> responseObserver) {
        Optional<ShippingMethod> methodOpt = shippingMethodRepository.findById(request.getShippingMethodId());
        if (methodOpt.isEmpty()) {
            responseObserver.onError(Status.NOT_FOUND.withDescription("Shipping method is not found").asRuntimeException());
            return;
        }

        ShippingMethod method = methodOpt.get();
        if(!method.getIsActive()){
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("Shipping method is not active").asRuntimeException());
            return;
        }

        responseObserver.onNext(ValidateShippingResponse.newBuilder().build());
        responseObserver.onCompleted();
    }
}
