package com.gtel.warehouse.grpc.impl;

import com.gtel.warehouse.entity.WalletEntity;
import com.gtel.warehouse.grpc.GetWalletDetailRequest;
import com.gtel.warehouse.grpc.GetWalletDetailResponse;
import com.gtel.warehouse.grpc.WalletGrpcServiceGrpc;
import com.gtel.warehouse.repository.WalletRepository;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.grpc.server.service.GrpcService;

import java.util.Optional;

@GrpcService
@Slf4j
@RequiredArgsConstructor
public class WalletGrpcServiceImpl extends WalletGrpcServiceGrpc.WalletGrpcServiceImplBase {
    private final WalletRepository walletRepository;

    @Override
    public void getWalletDetail(GetWalletDetailRequest request, StreamObserver<GetWalletDetailResponse> responseObserver) {
        Optional<WalletEntity> walletyOptional = walletRepository.findByUserId(request.getUserId());
        if(walletyOptional.isEmpty()) {
            responseObserver.onError(Status.NOT_FOUND.withDescription("Wallet not found").asRuntimeException());
            return;
        }

        WalletEntity wallet = walletyOptional.get();

        GetWalletDetailResponse response = GetWalletDetailResponse.newBuilder()
                .setId(wallet.getId())
                .setBalance(wallet.getBalance().toString())
                .setUserId(wallet.getUserId())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
