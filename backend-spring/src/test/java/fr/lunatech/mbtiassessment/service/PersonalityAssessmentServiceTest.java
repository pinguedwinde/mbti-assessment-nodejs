package fr.lunatech.mbtiassessment.service;

import fr.lunatech.mbtiassessment.dto.AnswerDTO;
import fr.lunatech.mbtiassessment.dto.PersonageDTO;
import fr.lunatech.mbtiassessment.dto.PersonalityAssessmentDTO;
import fr.lunatech.mbtiassessment.dto.PersonalityInfoDTO;
import fr.lunatech.mbtiassessment.mapper.PersonalityAssessmentMapper;
import fr.lunatech.mbtiassessment.model.*;
import fr.lunatech.mbtiassessment.model.util.Factor;
import fr.lunatech.mbtiassessment.model.util.Universe;
import fr.lunatech.mbtiassessment.repository.PersonalityAssessmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class PersonalityAssessmentServiceTest {

    private PersonalityAssessmentService service;

    @MockBean
    private PersonalityAssessmentRepository personalityAssessmentRepository;
    @MockBean
    private PersonalityInfoService personalityInfoService;
    @MockBean
    private QuestionService questionService;
    @MockBean
    private PersonageService personageService;

    List<PersonalityAssessmentDTO> personalityAssessmentDTOS = new ArrayList<>();
    private PersonalityAssessmentDTO assessmentDTO;

    private List<PersonageDTO> personageDTOS;
    private PersonageDTO personDTO1;
    private PersonageDTO personDTO2;

    @BeforeEach
    public void setup() {
        List<AnswerDTO> answerDTOS = new ArrayList<>();
        answerDTOS.add(new AnswerDTO(7, "60942911173d8200115b1241"));
        answerDTOS.add(new AnswerDTO(6, "60942911173d8200115b1237"));
        answerDTOS.add(new AnswerDTO(4, "60942911173d8200115b1230"));
        answerDTOS.add(new AnswerDTO(6, "60942911173d8200115b1255"));


        personDTO1 = new PersonageDTO("78c3c426", "Miss Tanya", "First Personality, She is Singer AfroTrap Diva", "INTP_A", Universe.MARVEL.title);
        personDTO2 = new PersonageDTO("e00fe410", "Monkey D Luffy", "Second Personality, kaizoku oni ore wa naru : of One Piece.", "INTP_A", Universe.PIRATES.title);
        personageDTOS = new ArrayList<>();
        personageDTOS.add(personDTO1);
        personageDTOS.add(personDTO2);

        Score score = new Score();
        score.setExtraversion(38);
        score.setIntuition(56);
        score.setFeeling(55);
        score.setJudging(44);
        score.setAssertive(70);

        PersonalityInfoDTO personalityInfoDTO1 = new PersonalityInfoDTO("60dc32f30e87bc68831908d2", "INTP_A", "Logician", "Analyst", "Innovative inventors with an unquenchable thirst for knowledge.", personageDTOS);

        assessmentDTO = new PersonalityAssessmentDTO("78c3c426", answerDTOS, score, personalityInfoDTO1);
        personalityAssessmentDTOS.add(assessmentDTO);

        service = new PersonalityAssessmentService(personalityAssessmentRepository, personalityInfoService, personageService, questionService);

    }

    @Test
    public void testGetPersonageByPersonalityAssessmentIdAndUniverse() {

        String assessmentId = "d6964805";

        Mockito.when(personalityAssessmentRepository.findById(assessmentId)).thenReturn(Mono.just(PersonalityAssessmentMapper.toEntity(assessmentDTO)));
        Mockito.when(personageService.getPersonagesByPersonalityType(assessmentDTO.getPersonalityInfoDTO().getPersonalityType())).thenReturn(Flux.just(personDTO1, personDTO2));

        Flux<PersonageDTO> personageDTOFlux = service.getPersonageByPersonalityAssessmentIdAndUniverse(assessmentId, "MARVEL");
        StepVerifier.create(personageDTOFlux)
                .expectNext(personageDTOS.get(0))
                .expectComplete()
                .verify();
    }

    @Test
    public void testGetScore() {
        // Given
        Score score = new Score();
        score.setExtraversion(38);
        score.setIntuition(56);
        score.setFeeling(55);
        score.setJudging(44);
        score.setAssertive(70);
        // When
        String personalityType = this.service.getPersonalityType(score);
        // Then
        assertThat(personalityType).isEqualTo("INFP_A");

    }

    @Test
    public void testProcessFactorScoreFromAnswers() {
        // Given
        List<Answer> answers = new ArrayList<>();
        Question extraversion = new Question("78c3c426", ".", Factor.EXTRAVERSION);
        Question introversion = new Question("78c3c426", ".", Factor.EXTRAVERSION);
        answers.add(new Answer(5, extraversion));
        answers.add(new Answer(6, extraversion));
        answers.add(new Answer(6, extraversion));
        answers.add(new Answer(4, extraversion));
        answers.add(new Answer(1, extraversion));

        answers.add(new Answer(2, introversion));
        answers.add(new Answer(1, introversion));
        answers.add(new Answer(3, introversion));
        answers.add(new Answer(6, introversion));
        answers.add(new Answer(6, introversion));

        // When
        int factorScore = this.service.processFactorScoreFromAnswers
                (answers, Factor.EXTRAVERSION, Factor.INTROVERSION);
        // Then
        assertThat(factorScore).isEqualTo(57);
    }
}
