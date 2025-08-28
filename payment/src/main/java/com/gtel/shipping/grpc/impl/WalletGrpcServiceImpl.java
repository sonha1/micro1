package com.gtel.shipping.grpc.impl;

import com.gtel.shipping.entity.WalletEntity;
import com.gtel.shipping.grpc.GetWalletDetailRequest;
import com.gtel.shipping.grpc.GetWalletDetailResponse;
import com.gtel.shipping.grpc.WalletGrpcServiceGrpc;
import com.gtel.shipping.repository.WalletRepository;
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
