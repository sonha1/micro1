package vn.gtel.user.grpc.impl;


import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.grpc.server.service.GrpcService;
import vn.gtel.user.entities.UserEntity;
import vn.gtel.user.grpc.GetUserDetailGrpcRequest;
import vn.gtel.user.grpc.GetUserDetailGrpcResponse;
import vn.gtel.user.grpc.UserGrpcServiceGrpc;
import vn.gtel.user.repositories.UserRepository;

import java.util.Objects;
import java.util.Optional;

@GrpcService
@RequiredArgsConstructor
@Slf4j
public class UserGrpcServiceImpl extends UserGrpcServiceGrpc.UserGrpcServiceImplBase {
    private final UserRepository userRepository;

    @Override
    public void getUserDetail(GetUserDetailGrpcRequest request, StreamObserver<GetUserDetailGrpcResponse> responseObserver) {
        Optional<UserEntity> userOptional = userRepository.findById(request.getId());
        if(userOptional.isEmpty()) {
            responseObserver.onError(Status.NOT_FOUND.withDescription("User not found").asRuntimeException());
            return;
        }

        UserEntity user = userOptional.get();
        GetUserDetailGrpcResponse response = GetUserDetailGrpcResponse
                .newBuilder()
                .setId(user.getId())
                .setUsername(user.getUsername())
                .setEmail(user.getEmail())
                .setPhoneNumber(Objects.requireNonNullElse(user.getPhoneNumber(), ""))
                .setFullName(Objects.requireNonNullElse(user.getFullName(), ""))
                .setStatus(user.getStatus())
                .setBalance(user.getBalance().toString())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
