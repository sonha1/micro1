package com.gtel.shipping.service;


import com.gtel.order.grpc.GetOrderDetailRequestGrpc;
import com.gtel.order.grpc.GetOrderDetailResponseGrpc;
import com.gtel.order.grpc.OrderGrpcServiceGrpc;
import com.gtel.shipping.entity.BillEntity;
import com.gtel.shipping.entity.WalletEntity;
import com.gtel.shipping.models.request.HandlePaymentEventMessage;
import com.gtel.shipping.repository.BillRepository;
import com.gtel.shipping.repository.WalletRepository;
import com.gtel.shipping.utils.ApplicationException;
import com.gtel.shipping.utils.ERROR_CODE;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class WalletService {
    private final WalletRepository walletRepository;
    private final BillRepository billRepository;

    private OrderGrpcServiceGrpc.OrderGrpcServiceBlockingStub orderServiceBlockingStub;

    public void handlePaymentEvent(HandlePaymentEventMessage message) {
        GetOrderDetailResponseGrpc orderDetail = orderServiceBlockingStub.getOrderDetail(GetOrderDetailRequestGrpc.newBuilder().setOrderId(message.getOrderId()).build());

        Optional<WalletEntity> walletyOptional = walletRepository.findByUserId(orderDetail.getUserId());
        if (walletyOptional.isEmpty()) {
            throw new ApplicationException(ERROR_CODE.RESOURCE_NOT_FOUND);
        }

        // check balance
        WalletEntity wallet = walletyOptional.get();
        if (wallet.getBalance().compareTo(new BigDecimal(orderDetail.getTotalAmount())) < 0) {
            throw new ApplicationException(ERROR_CODE.INVALID_PARAMETER, "Wallet not enough balance for payment");
        }

        createBill(orderDetail);

        // update wallet
        wallet.setBalance(wallet.getBalance().subtract(new BigDecimal(orderDetail.getTotalAmount())));
        walletRepository.save(wallet);
    }

    private void createBill(GetOrderDetailResponseGrpc orderDetail) {
        BillEntity bill = new BillEntity();
        bill.setUserId(orderDetail.getUserId());
        bill.setOrderId(orderDetail.getId());
        bill.setTotalAmount(new BigDecimal(orderDetail.getTotalAmount()));
        bill.setCreatedBy(orderDetail.getUserId());
        bill.setUpdatedBy(orderDetail.getUserId());
        billRepository.save(bill);
    }
}
