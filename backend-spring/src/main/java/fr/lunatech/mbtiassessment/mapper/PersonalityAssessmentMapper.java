package fr.lunatech.mbtiassessment.mapper;


import fr.lunatech.mbtiassessment.dto.PersonalityAssessmentDTO;
import fr.lunatech.mbtiassessment.model.PersonalityAssessment;

public class PersonalityAssessmentMapper {

    public static PersonalityAssessment toEntity(PersonalityAssessmentDTO personalityAssessmentDTO) {
        PersonalityAssessment personalityAssessment = new PersonalityAssessment();
        personalityAssessment.setId(personalityAssessmentDTO.getId());
        personalityAssessment.setAnswers(AnswerMapper.toEntities(personalityAssessmentDTO.getAnswerDTOs()));
        personalityAssessment.setScore(personalityAssessmentDTO.getScore());
        personalityAssessment.setPersonalityInfo(PersonalityInfoMapper.toEntity(personalityAssessmentDTO.getPersonalityInfoDTO()
        ));
        personalityAssessment.setTakenDate(personalityAssessmentDTO.getTakenDate());
        personalityAssessment.setUsername(personalityAssessmentDTO.getUsername());
        return personalityAssessment;
    }

    public static PersonalityAssessmentDTO toDto(PersonalityAssessment personalityAssessment) {
        PersonalityAssessmentDTO personalityAssessmentDTO = new PersonalityAssessmentDTO();
        personalityAssessmentDTO.setId(personalityAssessment.getId());
        personalityAssessmentDTO.setAnswerDTOs(AnswerMapper.toDtos(personalityAssessment.getAnswers()));
        personalityAssessmentDTO.setScore(personalityAssessment.getScore());
        personalityAssessmentDTO.setPersonalityInfoDTO(PersonalityInfoMapper.toDto(personalityAssessment.getPersonalityInfo()));
        personalityAssessmentDTO.setTakenDate(personalityAssessment.getTakenDate());
        personalityAssessmentDTO.setUsername(personalityAssessment.getUsername());
        return personalityAssessmentDTO;
    }
}
