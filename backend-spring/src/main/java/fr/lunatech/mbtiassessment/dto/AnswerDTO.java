package fr.lunatech.mbtiassessment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static fr.lunatech.mbtiassessment.security.validator.ValidatorsConstants.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDTO {
    @Min(value = ANSWER_MIN, message = "Answer" + MIN_VALUE + ANSWER_MIN)
    @Max(value = ANSWER_MAX, message = "Answer" + MAX_VALUE + ANSWER_MAX)
    private int answer;
    @NotNull(message = "Question ID" + NOT_NULL)
    @Size(min = ID_LENGTH, max = ID_LENGTH, message = ID_SIZE)
    private String questionId;
}
