package com.gtel.warehouse.grpc;

import com.gtel.warehouse.service.ProductService;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
@Slf4j
public class ProductGrpcImpl extends ProductGrpcServiceGrpc.ProductGrpcServiceImplBase {

    @Autowired
    private ProductService productService;


    @Override
    public void getProductInfo(GetProductInfoRequest request, StreamObserver<GetProductResponse> responseObserver) {
        try {
            GetProductResponse response = productService.getProductInfo(request);
            responseObserver.onNext(response);
        } catch (Exception e){
            GetProductResponse exResponse = GetProductResponse.newBuilder()
                    .setCode(500)
                    .setMessage("INTERNAL SERVER ERROR")
                    .build();

            responseObserver.onNext(exResponse);
        }
        responseObserver.onCompleted();
    }
}
