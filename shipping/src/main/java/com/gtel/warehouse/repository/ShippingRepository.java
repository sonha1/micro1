package com.gtel.warehouse.repository;

import com.gtel.warehouse.entity.ShippingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingRepository extends JpaRepository<ShippingEntity, Long> {

}



