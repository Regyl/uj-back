package com.github.regyl.unfriendlyjarvis.configuration.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * JWT configuration properties for token validation.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.application.jwt")
public class JwtProperties {

    /**
     * Secret key for validating JWT tokens.
     */
    private String secret;

    /**
     * Token issuer.
     */
    private String issuer;
}
