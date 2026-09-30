package com.github.regyl.unfriendlyjarvis.entity;

import com.github.regyl.unfriendlyjarvis.enumeration.AuthorityEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

/**
 * Provided user's authority entity.
 */
@Data
@Entity
@Table(name = "e_authority")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AuthorityEntity extends AbstractEntity implements GrantedAuthority {

    @NotEmpty
    @Column(name = "value", nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private AuthorityEnum value;

    @Override
    public String getAuthority() {
        return value.name();
    }
}
