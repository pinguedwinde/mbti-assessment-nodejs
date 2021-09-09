package fr.lunatech.mbtiassessment.repository;

import fr.lunatech.mbtiassessment.model.Question;
import fr.lunatech.mbtiassessment.model.util.Factor;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface QuestionRepository extends ReactiveMongoRepository<Question, String> {
    Flux<Question> findByFactor(Factor factor);
}
