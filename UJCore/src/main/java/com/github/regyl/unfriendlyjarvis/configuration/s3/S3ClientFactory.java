package com.github.regyl.unfriendlyjarvis.configuration.s3;

import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3ClientFactory {

    @Bean
    public MinioClient getMinioClient(S3ConfigurationProperties configProps) {
        return MinioClient.builder()
                .endpoint(configProps.getEndpoint())
                .credentials(configProps.getAccessKey(), configProps.getSecretKey())
                .region(configProps.getRegion())
                .build();
    }
}
