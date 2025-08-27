package com.gtel.order.grpc.service;


import com.gtel.order.entity.Order;
import com.gtel.order.entity.OrderItem;
import com.gtel.order.grpc.GetOrderDetailRequestGrpc;
import com.gtel.order.grpc.GetOrderDetailResponseGrpc;
import com.gtel.order.grpc.OrderGrpcServiceGrpc;
import com.gtel.order.grpc.OrderItemGrpc;
import com.gtel.order.repository.OrderItemRepository;
import com.gtel.order.repository.OrderRepository;
import com.gtel.order.service.OrderService;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.Optional;

@GrpcService
@RequiredArgsConstructor
@Slf4j
public class OrderGrpcServiceImpl extends OrderGrpcServiceGrpc.OrderGrpcServiceImplBase {
    private final OrderRepository orderService;
    private final OrderItemRepository orderItemRepository;

    @Override
    public void getOrderDetail(GetOrderDetailRequestGrpc request, StreamObserver<GetOrderDetailResponseGrpc> responseObserver) {
        Optional<Order> orderOpt = orderService.findById(request.getOrderId());
        if (orderOpt.isEmpty()) {
            responseObserver.onError(Status.NOT_FOUND.withDescription("Order not found").asRuntimeException());
            return;
        }

        Order order = orderOpt.get();
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(request.getOrderId());
        if (orderItems.isEmpty()) {
            responseObserver.onError(Status.NOT_FOUND.withDescription("Order Item not found").asRuntimeException());
            return;
        }

        List<OrderItemGrpc> items = orderItems.stream().map(
                item -> OrderItemGrpc
                                .newBuilder()
                                .setProductId(item.getProductId())
                                .setQuantity(item.getQuantity())
                                .build()
        ).toList();

        GetOrderDetailResponseGrpc responseGrpc = GetOrderDetailResponseGrpc
                .newBuilder()
                .setId(order.getId())
                .setUserId(order.getUserId())
                .setStatus(order.getStatus())
                .addAllItems(items)
                .setTotalPrice(order.getTotalPrice().toString())
                .setTotalFee(order.getTotalFee().toString())
                .setTotalAmount(order.getTotalAmount().toString())
                .setNote(order.getNote())
                .setPhoneNumber(order.getPhoneNumber())
                .setAddress(order.getAddress())
                .build();

        responseObserver.onNext(responseGrpc);
        responseObserver.onCompleted();
    }
}
