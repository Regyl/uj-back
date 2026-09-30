package com.github.regyl.unfriendlyjarvis.configuration.health;

import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("garage")
@RequiredArgsConstructor
public class GarageHealthCheckComponent implements HealthIndicator {

    private final MinioClient minioClient;

    @Override
    public Health health() {
        Optional<Exception> e = ping();
        Health.Builder builder = Health.status(e.isPresent() ? Status.DOWN : Status.UP);
        if (e.isPresent()) {
            builder = builder.withDetail("err_msg", e.get().getMessage());
        }
        return builder.build();
    }

    private Optional<Exception> ping() {
        try {
            minioClient.listBuckets();
            return Optional.empty();
        } catch (Exception e) {
            return Optional.of(e);
        }
    }
}
