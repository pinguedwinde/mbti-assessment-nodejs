package fr.lunatech.mbtiassessment.mapper;

import fr.lunatech.mbtiassessment.dto.QuestionDTO;
import fr.lunatech.mbtiassessment.model.Question;
import fr.lunatech.mbtiassessment.model.util.Factor;

public class QuestionMapper {

    public static Question toEntity(QuestionDTO questionDTO) {
        Factor factor = Factor.getFactorByString(questionDTO.getFactor());
        Question question = new Question();
        question.setId(questionDTO.getId());
        question.setContent(questionDTO.getContent());
        question.setFactor(factor);
        return question;
    }

    public static QuestionDTO toDto(Question question) {
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setId(question.getId());
        questionDTO.setContent(question.getContent());
        questionDTO.setFactor(question.getFactor().name());
        return questionDTO;
    }

}
