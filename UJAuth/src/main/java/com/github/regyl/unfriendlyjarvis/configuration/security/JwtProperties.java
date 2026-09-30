package com.github.regyl.unfriendlyjarvis.configuration.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * JWT configuration properties.
 */
@Data
@ConfigurationProperties(prefix = "spring.application.jwt")
public class JwtProperties {

    /**
     * Secret key for signing JWT tokens.
     */
    private String secret;

    /**
     * Access token expiration time in milliseconds.
     * Default: 15 minutes (900000 ms).
     */
    private Long accessTokenExpirationMs;

    /**
     * Refresh token expiration time in milliseconds.
     * Default: 7 days (604800000 ms).
     */
    private Long refreshTokenExpirationMs;

    /**
     * Token issuer.
     */
    private String issuer;
}
