package fr.lunatech.mbtiassessment.repository;

import fr.lunatech.mbtiassessment.model.Personage;
import fr.lunatech.mbtiassessment.model.util.PersonalityType;
import fr.lunatech.mbtiassessment.model.util.Universe;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface PersonageRepository extends ReactiveMongoRepository<Personage, String> {
    Flux<Personage> findByPersonalityType(PersonalityType personalityType);
    Flux<Personage> findByUniverse(Universe universe);
}
