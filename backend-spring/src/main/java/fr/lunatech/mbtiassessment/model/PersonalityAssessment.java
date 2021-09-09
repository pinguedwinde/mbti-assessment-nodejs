package fr.lunatech.mbtiassessment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document("personality_tests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonalityAssessment {
    @Id
    private String id;
    private List<Answer> answers;
    private Score score;
    private PersonalityInfo personalityInfo;
    private Date takenDate;
    private String username;
}
