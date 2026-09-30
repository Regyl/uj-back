package com.github.regyl.unfriendlyjarvis.service.impl.converter;

import com.github.regyl.unfriendlyjarvis.controller.dto.RegistrationDto;
import com.github.regyl.unfriendlyjarvis.controller.dto.oauth.github.UserInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.BiFunction;

/**
 * Converter for GitHub user info to registration DTO.
 */
@Component
@RequiredArgsConstructor
public class UserInfoDtoToRegistrationDtoMapperServiceImpl implements BiFunction<UserInfoDto, String, RegistrationDto> {

    /**
     * Convert user info to internal application DTO.
     * Implements Function interface.
     *
     * @param userInfoDto   dto with GitHub user's information
     * @return              DTO used to transfer information about user that should be created
     */
    @Override
    public RegistrationDto apply(UserInfoDto userInfoDto, String s) {
        if (userInfoDto == null) {
            return null;
        }

        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setLogin(userInfoDto.getLogin());
        registrationDto.setEmail(s);

        return registrationDto;
    }
}
