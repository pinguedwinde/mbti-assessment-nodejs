
package fr.lunatech.mbtiassessment.repository;

import fr.lunatech.mbtiassessment.model.PersonalityAssessment;
import fr.lunatech.mbtiassessment.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface PersonalityAssessmentRepository extends ReactiveMongoRepository<PersonalityAssessment, String> {
    Mono<PersonalityAssessment> findFirstByUsernameOrderByTakenDateDesc(String username);
}

