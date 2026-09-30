package com.github.regyl.unfriendlyjarvis.service.impl.jwt;

import com.github.regyl.unfriendlyjarvis.configuration.security.JwtProperties;
import com.github.regyl.unfriendlyjarvis.entity.UserEntity;
import com.github.regyl.unfriendlyjarvis.enumeration.TokenType;
import com.github.regyl.unfriendlyjarvis.service.jwt.JwtTokenProviderService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for JWT token operations.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProviderServiceImpl implements JwtTokenProviderService {

    private static final String AUTHORITIES_KEY = "authorities";
    private static final String USER_ID_KEY = "userId";
    private static final String TOKEN_TYPE_KEY = "tokenType";

    private final JwtProperties jwtProperties;

    /**
     * Generate access token for user.
     *
     * @param user user entity
     * @return access token
     */
    @Override
    public String generateAccessToken(UserEntity user) {
        return generateToken(user, TokenType.ACCESS);
    }

    /**
     * Generate refresh token for user.
     *
     * @param user user entity
     * @return refresh token
     */
    @Override
    public String generateRefreshToken(UserEntity user) {
        return generateToken(user, TokenType.REFRESH);
    }

    /**
     * Generate token for user with specified type.
     *
     * @param user      user entity
     * @param tokenType token type (ACCESS or REFRESH)
     * @return JWT token
     */
    private String generateToken(UserEntity user, TokenType tokenType) {
        Date now = new Date(); //FIXME
        Date expiration = new Date(now.getTime()
                + (tokenType == TokenType.ACCESS
                        ? jwtProperties.getAccessTokenExpirationMs() 
                        : jwtProperties.getRefreshTokenExpirationMs()));

        String authorities = user.getAuthorities() != null
                ? user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(","))
                : "";

        return Jwts.builder()
                .subject(user.getEmail())
                .claim(USER_ID_KEY, user.getId())
                .claim(AUTHORITIES_KEY, authorities)
                .claim(TOKEN_TYPE_KEY, tokenType.name())
                .issuer(jwtProperties.getIssuer())
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Validate token.
     *
     * @param token JWT token
     * @return true if token is valid
     */
    @Override
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
     * Get username from token.
     *
     * @param token JWT token
     * @return username (email)
     */
    @Override
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
    @Override
    public String getTokenTypeFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get(TOKEN_TYPE_KEY, String.class);
    }

    /**
     * Get authorities from token.
     *
     * @param token JWT token
     * @return list of authority strings
     */
    @Override
    public List<String> getAuthoritiesFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        String authorities = claims.get(AUTHORITIES_KEY, String.class);
        if (!StringUtils.hasLength(authorities)) {
            return Collections.emptyList();
        }
        return Arrays.asList(authorities.split(","));
    }

    /**
     * Check if token is refresh token.
     *
     * @param token JWT token
     * @return true if token is refresh token
     */
    @Override
    public boolean isRefreshToken(String token) {
        String tokenType = getTokenTypeFromToken(token);
        return TokenType.REFRESH.name().equals(tokenType);
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

