package fr.lunatech.mbtiassessment.security.domain;

import fr.lunatech.mbtiassessment.security.validator.CustomValidPassword;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static fr.lunatech.mbtiassessment.security.validator.ValidatorsConstants.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserForm {
    @NotBlank(message = "First name" + NOT_BLANK)
    @Size(min=NAME_MIN, max=NAME_MAX, message = "First name" + NAME_SIZE)
    private String firstName;

    @NotBlank(message = "Last name" + NOT_BLANK)
    @Size(min=NAME_MIN, max=NAME_MAX, message = "Last name" + NAME_SIZE)
    private String lastName;

    @NotBlank(message = "Username" + NOT_BLANK)
    @Size(min=UNAME_MIN, max=NAME_MAX, message = "Username" + UNAME_SIZE)
    private String username;

    @Size(min=UNAME_MIN, max=NAME_MAX, message = "Username" + UNAME_SIZE)
    private String currentUsername;

    @NotBlank(message = "Password" + NOT_BLANK)
    @Size(min=PASSWORD_MIN, max=PASSWORD_MAX, message = "Password" + PASSWORD_SIZE)
    @CustomValidPassword
    private String password;

    @NotBlank(message = "Email" + NOT_BLANK)
    @Pattern(regexp = "^[a-z0-9._%+-]+@[a-z0-9.-]+.[a-z]{2,4}$",message = VALID_EMAIL)
    @Size(max = EMAIL_MAX, message = EMAIL_SIZE)
    private String email;

    private Boolean isEnabled;
    private Boolean isNonLocked;
    private String role;
}
