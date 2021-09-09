package fr.lunatech.mbtiassessment.service;

import fr.lunatech.mbtiassessment.dto.AnswerDTO;
import fr.lunatech.mbtiassessment.dto.PersonageDTO;
import fr.lunatech.mbtiassessment.dto.PersonalityAssessmentDTO;
import fr.lunatech.mbtiassessment.mapper.AnswerMapper;
import fr.lunatech.mbtiassessment.mapper.PersonalityAssessmentMapper;
import fr.lunatech.mbtiassessment.mapper.QuestionMapper;
import fr.lunatech.mbtiassessment.model.*;
import fr.lunatech.mbtiassessment.model.util.Factor;
import fr.lunatech.mbtiassessment.repository.PersonalityAssessmentRepository;
import fr.lunatech.mbtiassessment.service.dataloader.PersonalityInfoDataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Objects;

@Service
public class PersonalityAssessmentService {

    private static final Logger log = LoggerFactory.getLogger(PersonalityInfoDataLoader.class);

    private final PersonalityAssessmentRepository repository;
    private final PersonalityInfoService personalityInfoService;
    private final PersonageService personageService;
    private final QuestionService questionService;


    public PersonalityAssessmentService(PersonalityAssessmentRepository repository, PersonalityInfoService personalityInfoService, PersonageService personageService, QuestionService questionService) {
        this.repository = repository;
        this.personalityInfoService = personalityInfoService;
        this.personageService = personageService;
        this.questionService = questionService;
    }

    public Mono<PersonalityAssessmentDTO> addPersonalityAssessment(PersonalityAssessmentDTO personalityAssessmentDTO) {
        PersonalityAssessment personalityAssessment = PersonalityAssessmentMapper.toEntity(personalityAssessmentDTO);
        return this.repository.save(personalityAssessment)
                .map(PersonalityAssessmentMapper::toDto)
                .subscribeOn(Schedulers.parallel());
    }

    public Mono<PersonalityAssessmentDTO> getPersonalityAssessmentById(String id) {
        return this.repository.findById(id)
                .map(PersonalityAssessmentMapper::toDto)
                .subscribeOn(Schedulers.parallel());
    }


    public Mono<PersonalityAssessmentDTO> getPersonalityAssessmentByUsername(String username) {
        return this.repository.findFirstByUsernameOrderByTakenDateDesc(username)
                .map(PersonalityAssessmentMapper::toDto)
                .subscribeOn(Schedulers.parallel());
    }
  
 
    public Flux<PersonageDTO> getPersonageByPersonalityAssessmentIdAndUniverse(String id, String universeName) {
        return this.repository.findById(id)
                .map(assessment -> assessment.getPersonalityInfo().getPersonalityType())
                .flux()
                .flatMap(personalityType ->
                        this.personageService.getPersonagesByPersonalityType(personalityType.label)
                                .filter(personage -> personage.getUniverse().equals(universeName))

                )
                .subscribeOn(Schedulers.parallel());
    }

    public Flux<PersonalityAssessmentDTO> getAllPersonalityAssessments() {
        return repository.findAll()
                .map(PersonalityAssessmentMapper::toDto)
                .subscribeOn(Schedulers.parallel());
    }

    public Mono<PersonalityAssessmentDTO> processPersonalityAssessment(Flux<AnswerDTO> answerFlux, String username) {
        Score score = new Score();
        PersonalityAssessmentDTO personalityAssessmentDTO = new PersonalityAssessmentDTO();
        personalityAssessmentDTO.setUsername(username);
        return answerFlux
                .map(answerDTO -> {
                    personalityAssessmentDTO.getAnswerDTOs().add(answerDTO);
                    return questionService.getQuestionById(answerDTO.getQuestionId())
                            .map(questionDTO -> {
                                Answer answer = AnswerMapper.toEntity(answerDTO);
                                answer.setQuestion(QuestionMapper.toEntity(questionDTO));
                                return answer;
                            });
                })
                .flatMap(answerMono -> answerMono)
                .collectList()
                .flatMap(
                        answers -> {
                            score.setExtraversion(processFactorScoreFromAnswers(answers, Factor.EXTRAVERSION, Factor.INTROVERSION));
                            score.setIntuition(processFactorScoreFromAnswers(answers, Factor.INTUITION, Factor.SENSING));
                            score.setFeeling(processFactorScoreFromAnswers(answers, Factor.FEELING, Factor.THINKING));
                            score.setJudging(processFactorScoreFromAnswers(answers, Factor.JUDGING, Factor.PERCEIVING));
                            score.setAssertive(processFactorScoreFromAnswers(answers, Factor.ASSERTIVE, Factor.TURBULENT));
                            personalityAssessmentDTO.setScore(score);
                            String personalityType = getPersonalityType(score);
                            return this.personalityInfoService.getPersonalityInfoByPersonalityType(personalityType)
                                    .flatMap(
                                            personalityInfoDTO -> {
                                                personalityAssessmentDTO.setPersonalityInfoDTO(personalityInfoDTO);
                                                return Mono.just(personalityAssessmentDTO);
                                            }
                                    )
                                    .flatMap(this::addPersonalityAssessment)
                                    .flatMap(
                                            assessmentDTO -> this.personageService.getPersonagesByPersonalityType(personalityType, 5)
                                                    .doOnNext(
                                                            personageDTO ->
                                                                    assessmentDTO.getPersonalityInfoDTO().getPersonageDTOs().add(personageDTO)
                                                    )
                                                    .then(Mono.just(assessmentDTO))
                                    );
                        }
                );
    }

    public int processFactorScoreFromAnswers(List<Answer> answers, Factor factor, Factor opposingFactor) {
        double factorSum = answers.stream().filter(answer -> Objects.equals(answer.getQuestion().getFactor(), factor))
                .mapToInt(Answer::getAnswer)
                .sum();
        factorSum += answers.stream().filter(answer -> Objects.equals(answer.getQuestion().getFactor(), opposingFactor))
                .mapToInt(
                        answer -> {
                            answer.setAnswer(8 - answer.getAnswer());
                            return answer.getAnswer();
                        }
                )
                .sum();
        long numberOfQuestion = answers.stream().filter(answer -> Objects.equals(answer.getQuestion().getFactor(), factor)
                || Objects.equals(answer.getQuestion().getFactor(), opposingFactor))
                .count();
        log.info("Total for " + factor.name() + " : " + factorSum);
        log.info("# of " + factor.name() + " questions : " + numberOfQuestion);
        int factorScore = getScore(factorSum, numberOfQuestion);
        log.info("[" + factor.name() + "] score : " + factorScore + "%");
        log.info("[" + opposingFactor.name() + "] score : " + (100 - factorScore) + "%");
        return factorScore;
    }

    private int getScore(double TotalFactor, long numberOfQuestion) {
        return (int) Math.round(100 * (TotalFactor / (7 * numberOfQuestion)));
    }

    public String getPersonalityType(Score score) {
        int extraversion = score.getExtraversion();
        int intuition = score.getIntuition();
        int feeling = score.getFeeling();
        int judging = score.getJudging();
        int assertive = score.getAssertive();
        String personalityType = "";
        if (extraversion == 0 || intuition == 0 || feeling == 0 || judging == 0 || assertive == 0) {
            return "UNDEFINED";
        } else {
            personalityType = extraversion > 50 ? personalityType.concat("E") : personalityType.concat("I");
            personalityType = intuition > 50 ? personalityType.concat("N") : personalityType.concat("S");
            personalityType = feeling > 50 ? personalityType.concat("F") : personalityType.concat("T");
            personalityType = judging > 50 ? personalityType.concat("J") : personalityType.concat("P");
            personalityType = assertive > 50 ? personalityType.concat("_A") : personalityType.concat("_T");
            return personalityType;
        }
    }
}
