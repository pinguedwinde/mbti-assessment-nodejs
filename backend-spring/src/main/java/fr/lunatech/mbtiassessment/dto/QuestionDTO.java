package fr.lunatech.mbtiassessment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

import static fr.lunatech.mbtiassessment.security.validator.ValidatorsConstants.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value = {"factor"}, allowSetters = true)
public class QuestionDTO implements Serializable {

    private String id;

    @NotBlank(message = "Content" + NOT_BLANK)
    @Size(max = DESC_MAX, message = "Content" + MAX_VALUE + DESC_MAX + ".")
    private String content;

    @JsonIgnoreProperties("factor")
    @NotBlank(message = "Factor" + NOT_BLANK)
    @Size(min = NAME_MIN, max = NAME_MAX, message = "Factor" + NAME_SIZE)
    private String factor;

    public QuestionDTO(String content, String factor) {
        this.content = content;
        this.factor = factor;
    }
}
