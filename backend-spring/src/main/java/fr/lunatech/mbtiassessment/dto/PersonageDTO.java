package fr.lunatech.mbtiassessment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static fr.lunatech.mbtiassessment.security.validator.ValidatorsConstants.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonageDTO {
    private String id;

    @NotBlank(message = "Name" + NOT_BLANK)
    @Size(min = NAME_MIN, max = NAME_MAX, message = "Name" + NAME_SIZE)
    private String name;

    @NotBlank(message = "About" + NOT_BLANK)
    @Size(max = DESC_MAX, message = "About" + MAX_VALUE + DESC_MAX + ".")
    private String about;

    @Pattern(regexp = "^[A-Z]{4}_[A-Z]$", message = PERSONALITY_TYPE_PATTERN)
    private String personalityType;

    @NotBlank(message = "Universe" + NOT_BLANK)
    @Size(min = NAME_MIN, max = NAME_MAX, message = "Universe" + NAME_SIZE)
    private String universe;

    public PersonageDTO(String name, String about, String personalityType, String universe) {
        this.name = name;
        this.about = about;
        this.personalityType = personalityType;
        this.universe = universe;
    }
}
