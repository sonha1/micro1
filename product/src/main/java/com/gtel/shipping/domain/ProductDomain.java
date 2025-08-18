package com.gtel.shipping.domain;

import com.gtel.shipping.entity.ProductEntity;
import com.gtel.shipping.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class ProductDomain {
    public ProductDomain(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    private final ProductRepository productRepository;


    public Optional<ProductEntity> findByIdProduct(long id){
        return productRepository.findById(id);
    }
}
