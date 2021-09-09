package fr.lunatech.mbtiassessment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

import static fr.lunatech.mbtiassessment.security.validator.ValidatorsConstants.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonalityInfoDTO {
    private String id;

    @Pattern(regexp = "^[A-Z]{4}_[A-Z]$", message = "Please give a valid PersonalityType as XXXX_X.")
    private String personalityType;

    @NotBlank(message = "Profile" + NOT_BLANK)
    @Size(min = NAME_MIN, max = NAME_MAX, message = "Profile" + NAME_SIZE)
    private String profile;

    @NotBlank(message = "Group" + NOT_BLANK)
    @Size(min = NAME_MIN, max = NAME_MAX, message = "Group" + NAME_SIZE)
    private String group;

    @NotBlank(message = "Description" + NOT_BLANK)
    @Size(max = DESC_MAX, message = "Description" + MAX_VALUE + DESC_MAX + ".")
    private String description;

    private List<@Valid PersonageDTO> personageDTOs;

    public PersonalityInfoDTO(String personalityType, String profile, String group, String description) {
        this.personalityType = personalityType;
        this.profile = profile;
        this.group = group;
        this.description = description;
    }

}
