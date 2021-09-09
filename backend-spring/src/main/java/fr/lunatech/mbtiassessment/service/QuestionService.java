package fr.lunatech.mbtiassessment.service;

import fr.lunatech.mbtiassessment.dto.QuestionDTO;
import fr.lunatech.mbtiassessment.error.domain.UndefinedFactorException;
import fr.lunatech.mbtiassessment.mapper.QuestionMapper;
import fr.lunatech.mbtiassessment.model.Question;
import fr.lunatech.mbtiassessment.model.util.Factor;
import fr.lunatech.mbtiassessment.repository.QuestionRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Collections;
import java.util.Objects;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public Mono<QuestionDTO> getQuestionById(String id) {
        return this.questionRepository.findById(id)
                .map(QuestionMapper::toDto)
                .subscribeOn(Schedulers.parallel());
    }

    public Flux<QuestionDTO> getAllQuestions() {
        return this.questionRepository.findAll()
                .map(QuestionMapper::toDto)
                .subscribeOn(Schedulers.parallel());
    }

    public Flux<QuestionDTO> getQuestionsByFactor(String factorName) {
        Factor factor = Factor.getFactorByString(factorName);
        if (factor == Factor.UNDEFINED) {
            return Flux.error(new UndefinedFactorException());
        } else {
            return this.questionRepository.findByFactor(factor)
                    .map(QuestionMapper::toDto)
                    .subscribeOn(Schedulers.parallel())
                    .switchIfEmpty(Flux.empty());
        }
    }

    public Flux<QuestionDTO> getQuestionnaire() {
        return getQuestionnaireByFactor(Factor.EXTRAVERSION)
                .mergeWith(getQuestionnaireByFactor(Factor.INTROVERSION))
                .mergeWith(getQuestionnaireByFactor(Factor.INTUITION))
                .mergeWith(getQuestionnaireByFactor(Factor.SENSING))
                .mergeWith(getQuestionnaireByFactor(Factor.FEELING))
                .mergeWith(getQuestionnaireByFactor(Factor.THINKING))
                .mergeWith(getQuestionnaireByFactor(Factor.TURBULENT))
                .mergeWith(getQuestionnaireByFactor(Factor.ASSERTIVE))
                .mergeWith(getQuestionnaireByFactor(Factor.JUDGING))
                .mergeWith(getQuestionnaireByFactor(Factor.PERCEIVING))
                .collectList()
                .flatMap(questions -> {
                    Collections.shuffle(questions);
                    return Mono.just(questions);
                })
                .flatMapMany(Flux::fromIterable);

    }

    private Flux<QuestionDTO> getQuestionnaireByFactor(Factor factor) {
        long NUMBER_OF_QUESTIONS = 5L;
        return this.questionRepository.findByFactor(factor)
                .collectList()
                .flatMap(questions -> {
                    Collections.shuffle(questions);
                    return Mono.just(questions);
                })
                .flatMapMany(Flux::fromIterable)
                .take(NUMBER_OF_QUESTIONS)
                .map(QuestionMapper::toDto)
                .subscribeOn(Schedulers.parallel());
    }

    public Mono<QuestionDTO> addQuestion(QuestionDTO questionDTO) {
        Question question = QuestionMapper.toEntity(questionDTO);
        if (question.getFactor() == Factor.UNDEFINED) {
            return Mono.error(new UndefinedFactorException());
        } else {
            return this.questionRepository.save(question)
                    .map(QuestionMapper::toDto)
                    .subscribeOn(Schedulers.parallel());
        }
    }

    public Flux<QuestionDTO> addQuestions(Flux<QuestionDTO> questionDTOFlux) {
        return this.questionRepository.saveAll(
                questionDTOFlux.map(QuestionMapper::toEntity)
                        .skipWhile(question -> Objects.equals(question.getFactor(), Factor.UNDEFINED))
        )
                .map(QuestionMapper::toDto)
                .subscribeOn(Schedulers.parallel());
    }

    public Mono<QuestionDTO> updateQuestion(QuestionDTO questionDTO) {
        Factor factor = Factor.getFactorByString(questionDTO.getFactor());
        if (factor == Factor.UNDEFINED) {
            return Mono.error(new UndefinedFactorException());
        } else {
            return this.questionRepository.findById(questionDTO.getId())
                    .flatMap(question -> this.questionRepository.save(QuestionMapper.toEntity(questionDTO)))
                    .map(QuestionMapper::toDto)
                    .subscribeOn(Schedulers.parallel())
                    .switchIfEmpty(Mono.empty());
        }
    }

    public Mono<Void> deleteQuestion(QuestionDTO questionDTO) {
        return this.questionRepository.delete(QuestionMapper.toEntity(questionDTO));
    }

    public Mono<Void> deleteAllQuestions() {
        return this.questionRepository.deleteAll();
    }

    public Mono<Long> count() {
        return this.questionRepository.count()
                .switchIfEmpty(Mono.just(0L));
    }
}
