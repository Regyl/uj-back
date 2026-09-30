package com.github.regyl.unfriendlyjarvis.service.impl.grpc;

import com.github.regyl.unfriendlyjarvis.grpc.GetAllSecretDataRequest;
import com.github.regyl.unfriendlyjarvis.grpc.GetAllSecretDataResponse;
import com.github.regyl.unfriendlyjarvis.grpc.GetSecretDataRequest;
import com.github.regyl.unfriendlyjarvis.grpc.GetSecretDataResponse;
import com.github.regyl.unfriendlyjarvis.grpc.SecretDataEntry;
import com.github.regyl.unfriendlyjarvis.grpc.UserSecretDataServiceGrpc;
import com.github.regyl.unfriendlyjarvis.service.usersecretdata.UserSecretDataService;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of UserSecretDataService using gRPC client.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserSecretDataServiceImpl implements UserSecretDataService {

    private final UserSecretDataServiceGrpc.UserSecretDataServiceBlockingStub userSecretDataServiceStub;
    
    @Override
    public Optional<String> getSecretData(Long userId, String key) {
        try {
            GetSecretDataRequest request = GetSecretDataRequest.newBuilder()
                    .setUserId(userId)
                    .setKey(key)
                    .build();
            
            GetSecretDataResponse response = userSecretDataServiceStub.getSecretData(request);
            
            if (response.getFound()) {
                return Optional.of(response.getValue());
            } else {
                log.debug("Secret data not found for user {} with key {}", userId, key);
                return Optional.empty();
            }
            
        } catch (StatusRuntimeException e) {
            Status status = e.getStatus();
            log.error("gRPC error while retrieving secret data for user {} with key {}: {}", 
                    userId, key, status.getDescription(), e);
            
            if (status.getCode() == Status.Code.NOT_FOUND) {
                return Optional.empty();
            }
            
            throw new RuntimeException("Failed to retrieve secret data: " + status.getDescription(), e);
        } catch (Exception e) {
            log.error("Unexpected error while retrieving secret data for user {} with key {}", 
                    userId, key, e);
            throw new RuntimeException("Failed to retrieve secret data", e);
        }
    }
    
    @Override
    public Map<String, String> getAllSecretData(Long userId) {
        try {
            GetAllSecretDataRequest request = GetAllSecretDataRequest.newBuilder()
                    .setUserId(userId)
                    .build();
            
            GetAllSecretDataResponse response =
                    userSecretDataServiceStub.getAllSecretData(request);
            
            if (response.getFound()) {
                return response.getEntriesList().stream()
                        .collect(Collectors.toMap(
                                SecretDataEntry::getKey,
                                SecretDataEntry::getValue
                        ));
            } else {
                log.debug("No secret data found for user {}", userId);
                return Collections.emptyMap();
            }
            
        } catch (StatusRuntimeException e) {
            Status status = e.getStatus();
            log.error("gRPC error while retrieving all secret data for user {}: {}", 
                    userId, status.getDescription(), e);
            
            if (status.getCode() == Status.Code.NOT_FOUND) {
                return Collections.emptyMap();
            }
            
            throw new RuntimeException("Failed to retrieve all secret data: " + status.getDescription(), e);
        } catch (Exception e) {
            log.error("Unexpected error while retrieving all secret data for user {}", userId, e);
            throw new RuntimeException("Failed to retrieve all secret data", e);
        }
    }
}

