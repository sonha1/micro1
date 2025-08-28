package com.gtel.shipping.repository;

import com.gtel.shipping.entity.StockTransactionEnity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockTransactionRepository extends JpaRepository<StockTransactionEnity, Long> {

}
