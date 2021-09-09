package fr.lunatech.mbtiassessment.service;

import fr.lunatech.mbtiassessment.dto.PersonalityInfoDTO;
import fr.lunatech.mbtiassessment.error.domain.UndefinedPersonalityTypeException;
import fr.lunatech.mbtiassessment.mapper.PersonalityInfoMapper;
import fr.lunatech.mbtiassessment.model.PersonalityInfo;
import fr.lunatech.mbtiassessment.model.util.PersonalityType;
import fr.lunatech.mbtiassessment.repository.PersonalityInfoRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Objects;

@Service
public class PersonalityInfoService {

    private final PersonalityInfoRepository repository;

    public PersonalityInfoService(PersonalityInfoRepository repository) {
        this.repository = repository;
    }

    public Mono<PersonalityInfoDTO> getPersonalityInfoById(String id) {
        return this.repository.findById(id)
                .map(PersonalityInfoMapper::toDto)
                .subscribeOn(Schedulers.parallel());
    }

    public Flux<PersonalityInfoDTO> getAllPersonalityInfos() {
        return this.repository.findAll()
                .map(PersonalityInfoMapper::toDto)
                .subscribeOn(Schedulers.parallel());
    }

    public Mono<PersonalityInfoDTO> getPersonalityInfoByPersonalityType(String personalityTypeName) {
        PersonalityType personalityType = PersonalityType.getPersonalityTypeByString(personalityTypeName);
        if (personalityType == PersonalityType.UNDEFINED) {
            return Mono.error(new UndefinedPersonalityTypeException());
        } else {
            return this.repository.findByPersonalityType(personalityType)
                    .map(PersonalityInfoMapper::toDto)
                    .subscribeOn(Schedulers.parallel());
        }
    }

    public Flux<PersonalityInfoDTO> getPersonalityInfoByProfile(String profile) {
        return this.repository.findByProfileIgnoreCase(profile)
                .map(PersonalityInfoMapper::toDto)
                .subscribeOn(Schedulers.parallel());
    }

    public Flux<PersonalityInfoDTO> getPersonalityInfoByGroup(String group) {
        return this.repository.findByGroupIgnoreCase(group)
                .map(PersonalityInfoMapper::toDto)
                .subscribeOn(Schedulers.parallel());
    }

    public Mono<PersonalityInfoDTO> addPersonalityInfo(PersonalityInfoDTO personalityInfoDTO) {
        PersonalityInfo personalityInfo = PersonalityInfoMapper.toEntity(personalityInfoDTO);
        if (personalityInfo.getPersonalityType() == PersonalityType.UNDEFINED) {
            return Mono.error(new UndefinedPersonalityTypeException());
        } else {
            return this.repository.save(personalityInfo)
                    .map(PersonalityInfoMapper::toDto)
                    .subscribeOn(Schedulers.parallel());
        }
    }

    public Flux<PersonalityInfoDTO> addPersonalityInfos(Flux<PersonalityInfoDTO> personalityInfoDTOFlux) {
        return this.repository.saveAll(
                personalityInfoDTOFlux.map(PersonalityInfoMapper::toEntity)
                        .skipWhile(personalityInfo -> Objects.equals(personalityInfo.getPersonalityType(), PersonalityType.UNDEFINED))
        )
                .map(PersonalityInfoMapper::toDto)
                .subscribeOn(Schedulers.parallel());
    }

    public Mono<PersonalityInfoDTO> updatePersonalityInfo(PersonalityInfoDTO personalityInfoDTO) {
        PersonalityType personalityType = PersonalityType.getPersonalityTypeByString(personalityInfoDTO.getPersonalityType());
        if (personalityType == PersonalityType.UNDEFINED) {
            return Mono.error(new UndefinedPersonalityTypeException());
        } else {
            return this.repository.findById(personalityInfoDTO.getId())
                    .flatMap(personalityInfo -> this.repository.save(PersonalityInfoMapper.toEntity(personalityInfoDTO)))
                    .map(PersonalityInfoMapper::toDto)
                    .subscribeOn(Schedulers.parallel());
        }
    }

    public Mono<Void> deletePersonalityInfo(PersonalityInfoDTO personalityInfoDTO) {
        return this.repository.delete(PersonalityInfoMapper.toEntity(personalityInfoDTO));
    }

    public Mono<Void> deleteAllPersonalityInfos() {
        return this.repository.deleteAll();
    }

    public Mono<Long> count() {
        return this.repository.count()
                .switchIfEmpty(Mono.just(0L));
    }

}
