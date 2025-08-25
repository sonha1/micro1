package com.gtel.warehouse.domain;

import com.gtel.warehouse.entity.ProductEntity;
import com.gtel.warehouse.repository.ProductRepository;
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
