package com.github.regyl.unfriendlyjarvis.service.impl.grpc;

import com.github.regyl.unfriendlyjarvis.entity.UserEntity;
import com.github.regyl.unfriendlyjarvis.entity.UserSecretDataEntity;
import com.github.regyl.unfriendlyjarvis.enumeration.UserSecretKey;
import com.github.regyl.unfriendlyjarvis.grpc.GetAllSecretDataRequest;
import com.github.regyl.unfriendlyjarvis.grpc.GetAllSecretDataResponse;
import com.github.regyl.unfriendlyjarvis.grpc.GetSecretDataRequest;
import com.github.regyl.unfriendlyjarvis.grpc.GetSecretDataResponse;
import com.github.regyl.unfriendlyjarvis.grpc.SecretDataEntry;
import com.github.regyl.unfriendlyjarvis.grpc.UserSecretDataServiceGrpc;
import com.github.regyl.unfriendlyjarvis.repository.UserRepository;
import com.github.regyl.unfriendlyjarvis.repository.UserSecretDataRepository;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.grpc.server.service.GrpcService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * gRPC service implementation for retrieving user secret data.
 */
@Slf4j
@GrpcService
@RequiredArgsConstructor
public class UserSecretDataGrpcServiceImpl extends UserSecretDataServiceGrpc.UserSecretDataServiceImplBase {
    
    private final UserSecretDataRepository userSecretDataRepository;
    private final UserRepository userRepository;
    
    @Override
    @Transactional(readOnly = true)
    public void getSecretData(
            GetSecretDataRequest request,
            StreamObserver<GetSecretDataResponse> responseObserver) {
        
        try {
            Long userId = request.getUserId();
            UserSecretKey key = UserSecretKey.fromKey(request.getKey());
            
            Optional<UserEntity> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                sendErrorResponse(responseObserver, Status.NOT_FOUND, 
                        "User not found with ID: " + request.getUserId());
                return;
            }
            
            Optional<UserSecretDataEntity> secretDataOpt =
                    userSecretDataRepository.findByUserAndKey(userOpt.get(), key);
            
            if (secretDataOpt.isEmpty()) {
                GetSecretDataResponse response =
                        GetSecretDataResponse.newBuilder()
                                .setFound(false)
                                .setErrorMessage("Secret data not found for key: " + key)
                                .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
                return;
            }
            
            UserSecretDataEntity secretData = secretDataOpt.get();
            GetSecretDataResponse response =
                    GetSecretDataResponse.newBuilder()
                            .setFound(true)
                            .setValue(secretData.getValue())
                            .build();
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            
        } catch (IllegalArgumentException e) {
            log.error("Invalid request parameters", e);
            sendErrorResponse(responseObserver, Status.INVALID_ARGUMENT, e.getMessage());
        } catch (Exception e) {
            log.error("Error retrieving secret data", e);
            sendErrorResponse(responseObserver, Status.INTERNAL, 
                    "Internal server error: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public void getAllSecretData(
            GetAllSecretDataRequest request,
            StreamObserver<GetAllSecretDataResponse> responseObserver) {
        
        try {
            Long userId = request.getUserId();
            
            Optional<UserEntity> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                sendErrorResponse(responseObserver, Status.NOT_FOUND, 
                        "User not found with ID: " + request.getUserId());
                return;
            }
            
            List<UserSecretDataEntity> secretDataList = userSecretDataRepository.findByUser_Id(userId);
            
            if (secretDataList.isEmpty()) {
                GetAllSecretDataResponse response =
                        GetAllSecretDataResponse.newBuilder()
                                .setFound(false)
                                .setErrorMessage("No secret data found for user: " + request.getUserId())
                                .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
                return;
            }
            
            List<SecretDataEntry> entries = secretDataList.stream()
                    .map(data -> SecretDataEntry.newBuilder()
                            .setKey(data.getKey().getKey())
                            .setValue(data.getValue())
                            .build())
                    .collect(Collectors.toList());
            
            GetAllSecretDataResponse response =
                    GetAllSecretDataResponse.newBuilder()
                            .setFound(true)
                            .addAllEntries(entries)
                            .build();
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            
        } catch (IllegalArgumentException e) {
            log.error("Invalid request parameters", e);
            sendErrorResponse(responseObserver, Status.INVALID_ARGUMENT, e.getMessage());
        } catch (Exception e) {
            log.error("Error retrieving all secret data", e);
            sendErrorResponse(responseObserver, Status.INTERNAL, 
                    "Internal server error: " + e.getMessage());
        }
    }
    
    private void sendErrorResponse(
            StreamObserver<?> responseObserver,
            Status status,
            String errorMessage) {
        
        responseObserver.onError(status.withDescription(errorMessage).asRuntimeException());
    }
}

