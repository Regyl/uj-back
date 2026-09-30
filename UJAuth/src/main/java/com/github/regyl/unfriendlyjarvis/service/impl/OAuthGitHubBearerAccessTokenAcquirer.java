package com.github.regyl.unfriendlyjarvis.service.impl;

import com.github.regyl.unfriendlyjarvis.controller.dto.oauth.OAuthInitializationDto;
import com.github.regyl.unfriendlyjarvis.controller.dto.oauth.github.AccessTokenRequestDto;
import com.github.regyl.unfriendlyjarvis.controller.dto.oauth.github.AccessTokenResponseDto;
import com.github.regyl.unfriendlyjarvis.enumeration.OAuthProviderType;
import com.github.regyl.unfriendlyjarvis.exceptiion.JarvisException;
import com.github.regyl.unfriendlyjarvis.feign.GitHubOAuthFeignClient;
import com.github.regyl.unfriendlyjarvis.service.impl.converter.OAuthInitializationToAccessTokenRequestMapper;
import com.github.regyl.unfriendlyjarvis.service.oauth.OAuthAccessTokenAcquirer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Implementation for acquiring GitHub <a href="https://oauth.net/2/bearer-tokens/">bearer</a> token.
 *
 * <p>
 * See also GitHub <a href="https://docs.github.com/en/apps/oauth-apps/building-oauth-apps/authorizing-oauth-apps">OAuth documentation</a>.
 */
@Component
@RequiredArgsConstructor
public class OAuthGitHubBearerAccessTokenAcquirer implements OAuthAccessTokenAcquirer {

    private static final String DEFAULT_TOKEN_TYPE = "Bearer";

    private final OAuthInitializationToAccessTokenRequestMapper oAuthInitializationMapper;
    private final GitHubOAuthFeignClient gitHubOAuthFeignClient;

    @Override
    public String acquire(OAuthInitializationDto initializationDto) {
        AccessTokenRequestDto requestDto = oAuthInitializationMapper.apply(initializationDto);
        AccessTokenResponseDto responseDto = gitHubOAuthFeignClient.getAccessToken(requestDto);
        
        if (!DEFAULT_TOKEN_TYPE.equalsIgnoreCase(responseDto.getTokenType())) {
            throw new JarvisException(responseDto.getErrorDescription());
        }

        return String.join(" ", DEFAULT_TOKEN_TYPE, responseDto.getAccessToken());
    }

    @Override
    public OAuthProviderType getSupportedProvider() {
        return OAuthProviderType.GitHub;
    }
}
