package com.gtel.shipping.repository;

import com.gtel.shipping.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity , Long> {
}
