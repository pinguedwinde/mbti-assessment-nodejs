package fr.lunatech.mbtiassessment.security.domain;

import fr.lunatech.mbtiassessment.security.validator.CustomValidPassword;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static fr.lunatech.mbtiassessment.security.validator.ValidatorsConstants.*;
import static fr.lunatech.mbtiassessment.security.validator.ValidatorsConstants.PASSWORD_SIZE;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCredentials {
    @NotBlank(message = "Username" + NOT_BLANK)
    @Size(min=UNAME_MIN, max=NAME_MAX, message = "Username" + UNAME_SIZE)
    private String username;

    @NotBlank(message = "Password" + NOT_BLANK)
    @Size(min=PASSWORD_MIN, max=PASSWORD_MAX, message = "Password" + PASSWORD_SIZE)
    @CustomValidPassword
    private String password;
}
