package com.gtel.order.repository;

import com.gtel.order.entity.Order;
import com.gtel.order.grpc.GetOrderDetailRequestGrpc;
import com.gtel.order.grpc.GetOrderDetailResponseGrpc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
