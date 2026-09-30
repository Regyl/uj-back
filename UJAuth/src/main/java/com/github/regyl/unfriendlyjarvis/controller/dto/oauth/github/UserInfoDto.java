package com.github.regyl.unfriendlyjarvis.controller.dto.oauth.github;

import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO with GitHub user's information.
 *
 * <p>
 * See also <a href="https://docs.github.com/en/rest/users/users?apiVersion=2022-11-28">GitHub API</a>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserInfoDto {

    private String login;

    private String avatarUrl;
}
