package fr.lunatech.mbtiassessment.dto;

import fr.lunatech.mbtiassessment.model.Score;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonalityAssessmentDTO {
    private String id;
    private List<AnswerDTO> answerDTOs = new ArrayList<>();
    private Score score;
    private PersonalityInfoDTO personalityInfoDTO;
    private Date takenDate = new Date();
    private String username;

    public PersonalityAssessmentDTO(String id, List<AnswerDTO> answerDTOS, Score score, PersonalityInfoDTO personalityInfoDTO) {
        this.id = id;
        this.answerDTOs = answerDTOS;
        this.score = score;
        this.personalityInfoDTO = personalityInfoDTO;
    }
}
