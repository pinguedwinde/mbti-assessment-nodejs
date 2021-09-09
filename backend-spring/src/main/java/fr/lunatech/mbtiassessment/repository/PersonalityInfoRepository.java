package fr.lunatech.mbtiassessment.repository;

import fr.lunatech.mbtiassessment.model.PersonalityInfo;
import fr.lunatech.mbtiassessment.model.util.PersonalityType;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PersonalityInfoRepository extends ReactiveMongoRepository<PersonalityInfo, String> {
    Mono<PersonalityInfo> findByPersonalityType(PersonalityType personalityType);

    Flux<PersonalityInfo> findByProfileIgnoreCase(String profile);

    Flux<PersonalityInfo> findByGroupIgnoreCase(String group);
}
