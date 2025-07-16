package com.gtel.order.grpc;

import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;
import com.gtel.product.grpc.*;

@Component
@Slf4j
public class ProductGrpcClient {

    @GrpcClient("product")
    com.gtel.product.grpc.ProductGrpcServiceGrpc.ProductGrpcServiceBlockingStub stub;

    public boolean validateProduct(long productId){
            GetProductInfoRequest request = GetProductInfoRequest
                    .newBuilder()
                    .setProductId(productId).build();

            GetProductResponse response = stub.getProductInfo(request);

            if (response.getCode() != 200){
                log.info("get product info error");
                return false;
            }

        return true;
    }
}
