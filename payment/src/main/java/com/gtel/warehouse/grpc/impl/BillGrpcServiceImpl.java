package com.gtel.warehouse.grpc.impl;

import com.gtel.warehouse.entity.BillEntity;
import com.gtel.warehouse.entity.WalletEntity;
import com.gtel.warehouse.grpc.BillGrpcServiceGrpc;
import com.gtel.warehouse.grpc.CreateBillRequest;
import com.gtel.warehouse.grpc.CreateBillResponse;
import com.gtel.warehouse.repository.BillRepository;
import com.gtel.warehouse.repository.WalletRepository;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.grpc.server.service.GrpcService;

import java.math.BigDecimal;
import java.util.Optional;

@GrpcService
@Slf4j
@RequiredArgsConstructor
public class BillGrpcServiceImpl extends BillGrpcServiceGrpc.BillGrpcServiceImplBase {
    private final BillRepository billRepository;
    private final WalletRepository walletRepository;

    @Override
    public void createBill(CreateBillRequest request, StreamObserver<CreateBillResponse> responseObserver) {
        // create lock

        // get wallet
        Optional<WalletEntity> walletyOptional = walletRepository.findByUserId(request.getUserId());
        if (walletyOptional.isEmpty()) {
            responseObserver.onError(Status.NOT_FOUND.withDescription("Wallet not found").asRuntimeException());
            return;
        }

        // check balance
        WalletEntity wallet = walletyOptional.get();
        if (wallet.getBalance().compareTo(new BigDecimal(request.getTotalAmount())) < 0) {
            responseObserver.onError(Status.INTERNAL.withDescription("Wallet not enough balance for payment").asRuntimeException());
            return;
        }

        // create bill
        BillEntity bill = create(request);

        // update wallet
        wallet.setBalance(wallet.getBalance().subtract(new BigDecimal(request.getTotalAmount())));
        walletRepository.save(wallet);

        // return response
        CreateBillResponse response =  CreateBillResponse.newBuilder().setMessage("Success").build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private BillEntity create(CreateBillRequest request) {
        BillEntity bill = new BillEntity();
        bill.setUserId(request.getUserId());
        bill.setOrderId(request.getOrderId());
        bill.setTotalAmount(new BigDecimal(request.getTotalAmount()));
        bill.setCreatedBy(request.getUserId());
        bill.setUpdatedBy(request.getUserId());
        billRepository.save(bill);
        return bill;
    }
}
