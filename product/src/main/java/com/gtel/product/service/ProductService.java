package com.gtel.product.service;

import com.gtel.product.domain.ProductDomain;
import com.gtel.product.entity.ProductEntity;
import com.gtel.product.grpc.GetProductInfoRequest;
import com.gtel.product.grpc.GetProductResponse;
import com.gtel.product.grpc.ProductInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class ProductService {

    @Autowired
    private ProductDomain productDomain;
//    GetProductInfoRequest request, StreamObserver<GetProductResponse>

    public GetProductResponse getProductInfo(GetProductInfoRequest request) {
        GetProductResponse.Builder responseBuilder = GetProductResponse.newBuilder();


        long productId = request.getProductId();

        Optional<ProductEntity> productOpt = productDomain.findByIdProduct(productId);


        if (productOpt.isEmpty()) {
            responseBuilder.setCode(404)
                    .setMessage("product not found");
            return responseBuilder.build();
        }


        ProductEntity productEntity = productOpt.get();
        ProductInfo.Builder productInfoBuilder = ProductInfo.newBuilder();

        productInfoBuilder
                .setProductId(productId)
                .setName(productEntity.getName())
                .setStatus(productEntity.getStatus());



        responseBuilder.setCode(200)
                .setMessage("SUCCESS")
                .setProduct(productInfoBuilder.build());
        return responseBuilder.build();
    }
}
