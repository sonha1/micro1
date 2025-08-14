package com.gtel.warehouse.repository;

import com.gtel.warehouse.entity.StockTransactionEnity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockTransactionRepository extends JpaRepository<StockTransactionEnity, Long> {

}
