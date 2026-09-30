package com.github.regyl.unfriendlyjarvis.controller.dto.oauth.github;

import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO to request GitHub access token.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AccessTokenRequestDto {

    private String clientId;

    private String clientSecret;

    private String code;

    private String redirectUri;
}
