package com.github.regyl.unfriendlyjarvis.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Set;

/**
 * User entity.
 */
@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "e_user")
@EqualsAndHashCode(callSuper = true)
public class UserEntity extends AbstractEntity implements OAuthUserDetails, UserDetails {

    /**
     * User's login. Must be optional.
     */
    @Column(name = "login", unique = true)
    private String login;

    @Email
    @NotNull
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password")
    private String password;
    
    @Column(name = "avatar_link")
    private String avatarLink;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<AuthorityEntity> authorities;
    
    /**
     * To support OAuth 2.0 authorization.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<ScopeEntity> scopes;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(name = "credentials_expiration_date")
    private LocalDate credentialsExpirationDate;

    @Column(name = "account_non_locked", columnDefinition = "boolean not null default true")
    private boolean accountNonLocked;

    @Column(name = "enabled", columnDefinition = "boolean not null default true")
    private boolean enabled;

    @Override
    public String getUsername() {
        return email;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return isDateNonExpired(expirationDate);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isDateNonExpired(credentialsExpirationDate);
    }

    private boolean isDateNonExpired(LocalDate date) {
        return date == null ? Boolean.TRUE
                : LocalDate.now(Clock.systemUTC()).isBefore(date);
    }
}
