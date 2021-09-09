package fr.lunatech.mbtiassessment.service;

import fr.lunatech.mbtiassessment.dto.PersonageDTO;
import fr.lunatech.mbtiassessment.dto.UniverseDTO;
import fr.lunatech.mbtiassessment.error.domain.UndefinedUniverseException;
import fr.lunatech.mbtiassessment.error.domain.UndefinedPersonalityTypeException;
import fr.lunatech.mbtiassessment.mapper.PersonageMapper;
import fr.lunatech.mbtiassessment.mapper.UniverseMapper;
import fr.lunatech.mbtiassessment.model.Personage;
import fr.lunatech.mbtiassessment.model.util.PersonalityType;
import fr.lunatech.mbtiassessment.model.util.Universe;
import fr.lunatech.mbtiassessment.repository.PersonageRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;

@Service
public class PersonageService {

    private final PersonageRepository personageRepository;

    public PersonageService(PersonageRepository personageRepository) {
        this.personageRepository = personageRepository;
    }

    public Mono<PersonageDTO> getPersonageById(String id){
        return this.personageRepository.findById(id)
                .map(PersonageMapper::toDto)
                .subscribeOn(Schedulers.parallel());
    }

    public Flux<PersonageDTO> getAllPersonages(){
        return this.personageRepository.findAll()
                .map(PersonageMapper::toDto)
                .subscribeOn(Schedulers.parallel());
    }

    public Flux<PersonageDTO> getPersonagesByPersonalityType(String personalityTypeName){
        personalityTypeName = personalityTypeName.replaceFirst("_T|_t", "_A");
        PersonalityType personalityType = PersonalityType.getPersonalityTypeByString(personalityTypeName);
        if (personalityType == PersonalityType.UNDEFINED) {
            return Flux.error(new UndefinedPersonalityTypeException());
        }else{
            return this.personageRepository.findByPersonalityType(personalityType)
                    .map(PersonageMapper::toDto)
                    .subscribeOn(Schedulers.parallel())
                    .switchIfEmpty(Flux.empty());
        }
    }

    public Flux<PersonageDTO> getPersonagesByPersonalityType(String personalityTypeName, long n){
        Random random = new Random();
        List<PersonageDTO> randomPersonages = new ArrayList<>();
        return this.getPersonagesByPersonalityType(personalityTypeName)
                .collectList()
                .map(
                        personageDTOs -> {
                            int size = personageDTOs.size();
                            Random r = new Random();
                            int index;
                            for (int i = 0; i < n; i++) {
                                index = random.nextInt(size);
                                randomPersonages.add(personageDTOs.get(index));
                            }
                            return Flux.fromIterable(randomPersonages);
                        }
                )
                .flatMapMany(Function.identity());
    }

    public Flux<UniverseDTO> getAllUniverse(){
        return Flux.fromArray(Universe.values())
                .map(UniverseMapper::toDto)
                .subscribeOn(Schedulers.parallel());

    }

    public Flux<PersonageDTO> getPersonagesByUniverse(String universeName) {
        Universe universe = Universe.getUniverseByString(universeName);
        if(universe == Universe.UNDEFINED) {
            return Flux.error(new UndefinedUniverseException());
        }else {
            return this.personageRepository.findByUniverse(universe)
                    .map(PersonageMapper::toDto)
                    .subscribeOn(Schedulers.parallel())
                    .switchIfEmpty(Flux.empty());
        }
    }

    public Flux<PersonageDTO> getPersonageByTypeAndUniverse(String personalityTypeName, String universeName) {
        PersonalityType personalityType = PersonalityType.getPersonalityTypeByString(personalityTypeName);
        Universe universe = Universe.getUniverseByString(universeName);
        if (personalityType == PersonalityType.UNDEFINED ) {
            return Flux.error(new UndefinedPersonalityTypeException());
        } if (universe == Universe.UNDEFINED ) {
            return Flux.error(new UndefinedUniverseException());
        } else {
            return this.personageRepository.findAll()
                    .filter(person -> person.getPersonalityType().equals(personalityType) && person.getUniverse().equals(universeName))
                    .map(PersonageMapper::toDto)
                    .subscribeOn(Schedulers.parallel())
                    .switchIfEmpty(Flux.empty());
        }
    }

    public Mono<PersonageDTO> addPersonage (PersonageDTO personageDTO){
        Personage personage = PersonageMapper.toEntity(personageDTO);
        if (personage.getPersonalityType() == PersonalityType.UNDEFINED) {
            return Mono.error(new UndefinedPersonalityTypeException());
        }else{
            return this.personageRepository.save(personage)
                    .map(PersonageMapper::toDto)
                    .subscribeOn(Schedulers.parallel());
        }
    }

    public Flux<PersonageDTO> addPersonages (Flux<PersonageDTO> personageDTOFlux){
        return this.personageRepository.saveAll(
                personageDTOFlux.map(PersonageMapper::toEntity)
                        .skipWhile(personage -> Objects.equals(personage.getPersonalityType(), PersonalityType.UNDEFINED))
                )
                .map(PersonageMapper::toDto)
                .subscribeOn(Schedulers.parallel());
    }

    public Mono<PersonageDTO> updatePersonage(PersonageDTO personageDTO){
        PersonalityType personalityType = PersonalityType.getPersonalityTypeByString(personageDTO.getPersonalityType());
        if (personalityType == PersonalityType.UNDEFINED) {
            return Mono.error(new UndefinedPersonalityTypeException());
        }else{
            return this.personageRepository.findById(personageDTO.getId())
                    .flatMap(personage -> this.personageRepository.save(PersonageMapper.toEntity(personageDTO)))
                    .map(PersonageMapper::toDto)
                    .subscribeOn(Schedulers.parallel())
                    .switchIfEmpty(Mono.empty());
        }
    }

    public Mono<Void> deletePersonage(String id){
        return this.personageRepository.deleteById(id);
    }

    public Mono<Void> deletePersonage(PersonageDTO personageDTO){
        return this.personageRepository.delete(PersonageMapper.toEntity(personageDTO));
    }

    public Mono<Void> deleteAllPersonages(){
        return this.personageRepository.deleteAll();
    }

    public Mono<Long> count(){
        return this.personageRepository.count()
                .switchIfEmpty(Mono.just(0L));
    }
}
