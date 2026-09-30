package com.github.regyl.unfriendlyjarvis.controller.dto.oauth;

import tools.jackson.databind.annotation.JsonDeserialize;
import com.github.regyl.unfriendlyjarvis.enumeration.OAuthProviderType;
import com.github.regyl.unfriendlyjarvis.service.impl.deserializer.OAuthProviderTypeDeserializer;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO with information to initialize OAuth 2.0 authorization.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OAuthInitializationDto {

    @NotEmpty
    private String code;
    
    private String state;

    @NotNull
    @JsonDeserialize(using = OAuthProviderTypeDeserializer.class)
    private OAuthProviderType oAuthProviderType;
}
