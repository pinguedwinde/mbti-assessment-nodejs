package fr.lunatech.mbtiassessment.service;

import fr.lunatech.mbtiassessment.dto.QuestionDTO;
import fr.lunatech.mbtiassessment.error.domain.UndefinedFactorException;
import fr.lunatech.mbtiassessment.mapper.QuestionMapper;
import fr.lunatech.mbtiassessment.model.Question;
import fr.lunatech.mbtiassessment.model.util.Factor;
import fr.lunatech.mbtiassessment.repository.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ActiveProfiles("test")
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class QuestionServiceTest {

    @MockBean
    private QuestionRepository repository;
    @Autowired
    private QuestionService service;

    private List<QuestionDTO> questionDTOS;
    private QuestionDTO assertive;
    private QuestionDTO introversion;
    private QuestionDTO feeling;

    @BeforeEach
    public void setup() {
        this.assertive = new QuestionDTO("78c3c426", "You rarely feel insecure.", "ASSERTIVE");
        this.introversion = new QuestionDTO("e00fe410", "You avoid making phone calls.", "INTROVERSION");
        this.feeling = new QuestionDTO("d6964805", "You are very sentimental.", "FEELING");
        this.questionDTOS = new ArrayList<>();
        this.questionDTOS.add(this.assertive);
        this.questionDTOS.add(this.feeling);
        this.questionDTOS.add(this.introversion);
        this.questionDTOS.add(new QuestionDTO("4f729859", "You often reflect on the reasons for human existence.", "INTUITION"));
        this.questionDTOS.add(new QuestionDTO("f537deed", "You enjoy watching people argue", "INTUITION"));
        this.questionDTOS.add(new QuestionDTO("2bfdc807", "You enjoy participating in group activities.", "EXTRAVERSION"));
        this.questionDTOS.add(new QuestionDTO("054f9674", "You rarely take action out of curiosity.", "SENSING"));
        this.questionDTOS.add(new QuestionDTO("50bc6ae8", "You are very sentimental.", "FEELING"));
        this.questionDTOS.add(new QuestionDTO("18975bf6", "You feel superior to others.", "THINKING"));
        this.questionDTOS.add(new QuestionDTO("83331d43", "You like to have a to-do list for each day.", "JUDGING"));
    }

    @Test
    public void testGetAllQuestions() {
        Mockito.when(this.repository.findAll())
                .thenReturn(Flux.fromIterable(questionDTOS).map(QuestionMapper::toEntity));
        Flux<QuestionDTO> questionDTOFlux = this.service.getAllQuestions();
        StepVerifier.create(questionDTOFlux)
                .expectNextSequence(this.questionDTOS)
                .verifyComplete();

        StepVerifier.create(questionDTOFlux)
                .expectNextCount(10)
                .expectComplete()
                .verify();
    }

    @Test
    public void testGetQuestionById() {
        String id = "d6964805";
        Mockito.when(this.repository.findById(id))
                .thenReturn(Mono.just(QuestionMapper.toEntity(this.feeling)));
        Mono<QuestionDTO> questionDTOMono = this.service.getQuestionById(id);
        StepVerifier.create(questionDTOMono)
                .expectNext(this.feeling)
                .verifyComplete();
    }

    @Test
    public void testGetQuestionByInvalidId() {
        String id = "d6964805-with-error";
        Mockito.when(this.repository.findById(id))
                .thenReturn(Mono.empty());
        Mono<QuestionDTO> questionDTOMono = this.service.getQuestionById(id);
        StepVerifier.create(questionDTOMono)
                .expectComplete()
                .verify();
    }

    @Test
    public void testGetQuestionsByFactor() {
        Factor factor = Factor.getFactorByString("intuition");
        Question q1 = QuestionMapper.toEntity(this.questionDTOS.get(3));
        Question q2 = QuestionMapper.toEntity(this.questionDTOS.get(4));
        Mockito.when(this.repository.findByFactor(factor))
                .thenReturn(Flux.just(q1, q2));
        Flux<QuestionDTO> questionDTOFlux = this.service.getQuestionsByFactor("intuition");

        StepVerifier.create(questionDTOFlux)
                .expectNext(this.questionDTOS.get(3))
                .expectNext(this.questionDTOS.get(4))
                .expectComplete()
                .verify();

        StepVerifier.create(questionDTOFlux)
                .expectNextCount(2)
                .expectComplete()
                .verify();
    }

    @Test
    public void testGetQuestionsByUndefinedFactor() {
        String factorName = "intuitionnnnnnnn";
        Flux<QuestionDTO> questionDTOFlux = this.service.getQuestionsByFactor(factorName);

        StepVerifier.create(questionDTOFlux)
                .expectError(UndefinedFactorException.class)
                .verify();
    }

    @Test
    public void testAddQuestion() {
        Question question = QuestionMapper.toEntity(this.assertive);
        Mockito.when(this.repository.save(question))
                .thenReturn(Mono.just(question));
        Mono<QuestionDTO> questionDTOMono = this.service.addQuestion(this.assertive);

        StepVerifier.create(questionDTOMono)
                .expectNext(this.assertive)
                .verifyComplete();
    }

    @Test
    public void testAddQuestionWithUndefinedFactor() {
        QuestionDTO questionDTO = new QuestionDTO("e00fe410", "You avoid making phone calls.", "INTROVERSION-WITH-ERROR");
        Mono<QuestionDTO> questionDTOMono = this.service.addQuestion(questionDTO);

        StepVerifier.create(questionDTOMono)
                .expectError(UndefinedFactorException.class)
                .verify();
    }

    @Test
    public void testUpdateQuestion() {
        String id = "e00fe410";
        Question question = QuestionMapper.toEntity(this.introversion);
        QuestionDTO questionDTO = this.introversion;
        questionDTO.setContent(questionDTO.getContent() + " Updated question!");
        Mockito.when(this.repository.findById(id)).thenReturn(Mono.just(question));
        Mockito.when(this.repository.save(QuestionMapper.toEntity(questionDTO)))
                .thenReturn(Mono.just(QuestionMapper.toEntity(questionDTO)));

        Mono<QuestionDTO> questionDTOMono = this.service.updateQuestion(this.introversion);

        StepVerifier.create(questionDTOMono)
                .expectNextMatches(dto -> Objects.equals(dto.getId(), id) &&
                        Objects.equals(dto.getContent(), "You avoid making phone calls. Updated question!") &&
                        Objects.equals(dto.getFactor(), "INTROVERSION"))
                .verifyComplete();
    }

    @Test
    public void testUpdateQuestionWithUndefined() {
        String id = "e00fe410";
        QuestionDTO questionDTO = this.introversion;
        questionDTO.setFactor("INTROVERSION-WITH-ERROR");
        Mono<QuestionDTO> questionDTOMono = this.service.updateQuestion(questionDTO);

        StepVerifier.create(questionDTOMono)
                .expectError(UndefinedFactorException.class)
                .verify();
    }

}
