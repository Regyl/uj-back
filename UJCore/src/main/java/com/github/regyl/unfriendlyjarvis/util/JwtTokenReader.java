package com.github.regyl.unfriendlyjarvis.util;

import com.github.regyl.unfriendlyjarvis.configuration.security.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Utility class for reading and validating JWT tokens.
 * Does not generate tokens, only reads and validates them.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenReader {

    private static final String AUTHORITIES_KEY = "authorities";
    private static final String USER_ID_KEY = "userId";
    private static final String TOKEN_TYPE_KEY = "tokenType";
    private static final String TOKEN_TYPE_ACCESS = "ACCESS";

    private final JwtProperties jwtProperties;

    /**
     * Validate token.
     *
     * @param token JWT token
     * @return true if token is valid
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    /**
     * Get username (email) from token.
     *
     * @param token JWT token
     * @return username (email)
     */
    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    /**
     * Get user ID from token.
     *
     * @param token JWT token
     * @return user ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get(USER_ID_KEY, Long.class);
    }

    /**
     * Get token type from token.
     *
     * @param token JWT token
     * @return token type
     */
    public String getTokenTypeFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get(TOKEN_TYPE_KEY, String.class);
    }

    /**
     * Check if token is access token.
     *
     * @param token JWT token
     * @return true if token is access token
     */
    public boolean isAccessToken(String token) {
        String tokenType = getTokenTypeFromToken(token);
        return TOKEN_TYPE_ACCESS.equals(tokenType);
    }

    /**
     * Get authorities from token.
     *
     * @param token JWT token
     * @return list of authority strings
     */
    public List<String> getAuthoritiesFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        String authorities = claims.get(AUTHORITIES_KEY, String.class);
        if (!StringUtils.hasLength(authorities)) {
            return Collections.emptyList();
        }
        return Arrays.asList(authorities.split(","));
    }

    /**
     * Get claims from token.
     *
     * @param token JWT token
     * @return claims
     */
    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Get signing key from secret.
     *
     * @return secret key
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

