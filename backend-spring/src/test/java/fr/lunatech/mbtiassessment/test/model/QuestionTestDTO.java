package fr.lunatech.mbtiassessment.test.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionTestDTO {
    private String id;
    private String content;
    private String factor;

    public QuestionTestDTO(String content, String factor) {
        this.content = content;
        this.factor = factor;
    }
}
