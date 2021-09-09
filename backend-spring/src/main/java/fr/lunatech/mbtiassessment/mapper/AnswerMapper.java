package fr.lunatech.mbtiassessment.mapper;

import fr.lunatech.mbtiassessment.dto.AnswerDTO;
import fr.lunatech.mbtiassessment.model.Answer;
import fr.lunatech.mbtiassessment.model.Question;

import java.util.List;
import java.util.stream.Collectors;

public class AnswerMapper {
    public static Answer toEntity(AnswerDTO answerDTO) {
        Answer answer = new Answer();
        answer.setAnswer(answerDTO.getAnswer());
        Question question = new Question();
        question.setId(answerDTO.getQuestionId());
        answer.setQuestion(question);
        return answer;
    }

    public static List<Answer> toEntities(List<AnswerDTO> answerDTOs) {
        return answerDTOs.stream().map(AnswerMapper::toEntity)
                .collect(Collectors.toList());
    }

    public static AnswerDTO toDto(Answer answer) {
        AnswerDTO answerDTO = new AnswerDTO();
        answerDTO.setAnswer(answer.getAnswer());
        answerDTO.setQuestionId(answer.getQuestion().getId());
        return answerDTO;
    }

    public static List<AnswerDTO> toDtos(List<Answer> answers) {
        return answers.stream().map(AnswerMapper::toDto)
                .collect(Collectors.toList());
    }
}
