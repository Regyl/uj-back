package com.github.regyl.unfriendlyjarvis.configuration.s3;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spring.s3")
public class S3ConfigurationProperties {

    private String endpoint;

    private String accessKey;

    private String secretKey;

    private String region = "us-east-1";

    private Integer presignedUriTtlHours;
}
