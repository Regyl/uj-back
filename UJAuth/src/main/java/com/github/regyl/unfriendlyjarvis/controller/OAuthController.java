package com.github.regyl.unfriendlyjarvis.controller;

import com.github.regyl.unfriendlyjarvis.controller.dto.TokenResponseDto;
import com.github.regyl.unfriendlyjarvis.controller.dto.oauth.OAuthInitializationDto;
import com.github.regyl.unfriendlyjarvis.enumeration.OAuthProviderType;
import com.github.regyl.unfriendlyjarvis.exceptiion.JarvisException;
import com.github.regyl.unfriendlyjarvis.service.oauth.OAuthService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * OAuth 2.0 authorization.
 */
@RestController
@RequestMapping("/oauth")
public class OAuthController {

    private final Map<OAuthProviderType, OAuthService> oAuthServiceMap;

    /**
     * Constructor.
     *
     * @param oAuthServices list of OAuth services
     */
    public OAuthController(List<OAuthService> oAuthServices) {
        this.oAuthServiceMap = oAuthServices.stream()
                .collect(Collectors.toMap(OAuthService::getSupportedProvider, Function.identity()));
    }

    /**
     * Authorize by OAuth 2.0 provider.
     *
     * @param dto DTO with information about new user
     * @return                  JWT tokens (access and refresh) if user exists, null otherwise
     */
    @PostMapping("/sign-in")
    public TokenResponseDto signIn(@RequestBody @NotNull OAuthInitializationDto dto) {
        OAuthService oAuthService = getOAuthService(dto.getOAuthProviderType());

        return oAuthService.signIn(dto);
    }

    /**
     * Creates new user using OAuth 2.0 provider.
     *
     * @param dto DTO with information about new user
     * @return                JWT tokens (access and refresh)
     */
    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public TokenResponseDto signUp(@RequestBody @NotNull OAuthInitializationDto dto) {
        OAuthService oAuthService = getOAuthService(dto.getOAuthProviderType());

        return oAuthService.signUp(dto);
    }

    private OAuthService getOAuthService(OAuthProviderType providerType) {
        OAuthService oAuthService = oAuthServiceMap.get(providerType);
        if (oAuthService == null) {
            throw new JarvisException("Unsupported provider type " + providerType.name());
        }

        return oAuthService;
    }
}
