package com.github.regyl.unfriendlyjarvis.controller;

import com.github.regyl.unfriendlyjarvis.controller.dto.RefreshTokenRequestDto;
import com.github.regyl.unfriendlyjarvis.controller.dto.RegistrationDto;
import com.github.regyl.unfriendlyjarvis.controller.dto.TokenResponseDto;
import com.github.regyl.unfriendlyjarvis.service.AuthService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Basic username & password authorization.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/basic")
public class AuthController {

    private final AuthService authService;

    /**
     * Authorize by username/password.
     *
     * @param username  username
     * @param password  password
     * @return          JWT tokens (access and refresh)
     */
    @GetMapping("/sign-in")
    public TokenResponseDto signIn(@RequestParam("username") @NotEmpty String username,
                                    @RequestParam("password") @NotEmpty String password) {
        return authService.signIn(username, password);
    }

    /**
     * Creates new user.
     *
     * @param registrationDto DTO with information about new user
     * @return                JWT tokens (access and refresh)
     */
    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public TokenResponseDto signUp(@RequestBody @NotNull @Valid RegistrationDto registrationDto) {
        return authService.signUp(registrationDto);
    }

    /**
     * Refresh access token using refresh token.
     *
     * @param refreshTokenRequest DTO with refresh token
     * @return                   new JWT tokens (access and refresh)
     */
    @PostMapping("/refresh")
    public TokenResponseDto refreshToken(@RequestBody @NotNull @Valid RefreshTokenRequestDto refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest.getRefreshToken());
    }
}
