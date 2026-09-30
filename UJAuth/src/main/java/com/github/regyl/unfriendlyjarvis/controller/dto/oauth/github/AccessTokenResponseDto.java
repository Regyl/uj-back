package com.github.regyl.unfriendlyjarvis.controller.dto.oauth.github;

import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonNaming;
import com.github.regyl.unfriendlyjarvis.service.impl.deserializer.ScopeCollectionDeserializer;
import com.github.regyl.unfriendlyjarvis.enumeration.Scope;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

/**
 * DTO with user's access token and allowed scopes.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AccessTokenResponseDto {
    
    /**
     * Usually used as <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Authorization">Authorization</a> header.
     */
    private String accessToken;

    private String tokenType;

    @JsonDeserialize(using = ScopeCollectionDeserializer.class)
    private Collection<Scope> scope;

    private String error;

    private String errorDescription;

    private String errorUri;
}
