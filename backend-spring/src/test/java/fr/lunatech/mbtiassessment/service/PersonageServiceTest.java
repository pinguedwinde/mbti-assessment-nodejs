package fr.lunatech.mbtiassessment.service;

import fr.lunatech.mbtiassessment.dto.PersonageDTO;
import fr.lunatech.mbtiassessment.dto.UniverseDTO;
import fr.lunatech.mbtiassessment.error.domain.UndefinedPersonalityTypeException;
import fr.lunatech.mbtiassessment.mapper.PersonageMapper;
import fr.lunatech.mbtiassessment.mapper.UniverseMapper;
import fr.lunatech.mbtiassessment.model.Personage;
import fr.lunatech.mbtiassessment.model.util.PersonalityType;
import fr.lunatech.mbtiassessment.model.util.Universe;
import fr.lunatech.mbtiassessment.repository.PersonageRepository;
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
public class PersonageServiceTest {
    @MockBean
    private PersonageRepository repository;
    @Autowired
    private PersonageService service;

    private List<PersonageDTO> personageDTOS;
    private PersonageDTO tanya;
    private PersonageDTO luffy;
    private PersonageDTO arya;

    private UniverseDTO[] universeDTOS ;


    @BeforeEach
    public void setup () {
        this.tanya = new PersonageDTO("78c3c426", "Miss Tanya", "First Personality, She is Singer AfroTrap Diva", "INTJ_A", "");
        this.luffy = new PersonageDTO("e00fe410", "Monkey D Luffy", "Second Personality, kaizoku oni ore wa naru : of One Piece.", "ISTP_T", "");
        this.arya = new PersonageDTO("d6964805", "Arya Stark", "Third Personality, GOT", "INFJ_T", "GOT");
        this.personageDTOS = new ArrayList<>();
        this.personageDTOS.add(this.tanya);
        this.personageDTOS.add(this.arya);
        this.personageDTOS.add(this.luffy);
        this.personageDTOS.add(new PersonageDTO("4f729859", "James St Patrick the Ghost", "Fouth Personality, Power", "INFJ_T", ""));


        universeDTOS = new UniverseDTO[Universe.values().length];

        for(int i = 0; i < Universe.values().length; i++){
            universeDTOS[i]  = UniverseMapper.toDto(Universe.values()[i]);
        }

    }
    @Test
    public void testGetAllPersonages(){
        Mockito.when(this.repository.findAll())
                .thenReturn(Flux.fromIterable(personageDTOS).map(PersonageMapper::toEntity));
        Flux<PersonageDTO> personageDTOFlux = this.service.getAllPersonages();
        StepVerifier.create(personageDTOFlux)
                .expectNextSequence(this.personageDTOS)
                .verifyComplete();

        StepVerifier.create(personageDTOFlux)
                .expectNextCount(4)
                .expectComplete()
                .verify();
    }

    @Test
    public void testGetAllUniverse(){
        Flux<UniverseDTO> universeDTOFlux = service.getAllUniverse();
        StepVerifier.create(universeDTOFlux)
                .expectNext(universeDTOS)
                .verifyComplete();

        StepVerifier.create(universeDTOFlux)
                .expectNextCount(Universe.values().length)
                .expectComplete()
                .verify();
    }

    @Test
    public void testGetPersonageById(){
        String id = "e00fe410";
        Mockito.when(this.repository.findById(id))
                .thenReturn(Mono.just(PersonageMapper.toEntity(this.luffy)));
        Mono<PersonageDTO> personageDTOMono = this.service.getPersonageById(id);
        StepVerifier.create(personageDTOMono)
                .expectNext(this.luffy)
                .verifyComplete();
    }

    @Test
    public void testGetPersonageByInvalidId(){
        String id = "e00fe410-with-error";
        Mockito.when(this.repository.findById(id))
                .thenReturn(Mono.empty());
        Mono<PersonageDTO> personageDTOMono = this.service.getPersonageById(id);
        StepVerifier.create(personageDTOMono)
                .expectComplete()
                .verify();
    }

    //@Test
    public void testGetPersonagesByPersonalityType(){
        PersonalityType personalityType = PersonalityType.getPersonalityTypeByString("INFJ_T");
        Personage p1 = PersonageMapper.toEntity(this.personageDTOS.get(2));
        Personage p2 = PersonageMapper.toEntity(this.personageDTOS.get(3));
        Mockito.when(this.repository.findByPersonalityType(personalityType))
                .thenReturn(Flux.just(p1, p2));
        Flux<PersonageDTO> personageDTOFlux = this.service.getPersonagesByPersonalityType("INFJ_T");

        StepVerifier.create(personageDTOFlux)
                .expectNext(this.personageDTOS.get(2))
                .expectNext(this.personageDTOS.get(3))
                .expectComplete()
                .verify();

        StepVerifier.create(personageDTOFlux)
                .expectNextCount(2)
                .expectComplete()
                .verify();
    }

    @Test
    public void testGetPersonagesByUndefinedPersonalityType(){
        String personalityTypeName = "INFJ_T-WITH-ERROR";
        Flux<PersonageDTO> personageDTOFlux = this.service.getPersonagesByPersonalityType(personalityTypeName);

        StepVerifier.create(personageDTOFlux)
                .expectError(UndefinedPersonalityTypeException.class)
                .verify();
    }

    @Test
    public void testAddPersonage(){
        Personage personage = PersonageMapper.toEntity(this.tanya);
        Mockito.when(this.repository.save(personage))
                .thenReturn(Mono.just(personage));
        Mono<PersonageDTO> personageDTOMono = this.service.addPersonage(this.tanya);

        StepVerifier.create(personageDTOMono)
                .expectNext(this.tanya)
                .verifyComplete();
    }

    @Test
    public void testAddPersonageWithUndefinedPersonalityType(){
        this.tanya.setPersonalityType("ERROR");
        Mono<PersonageDTO> personageDTOMono = this.service.addPersonage(this.tanya);

        StepVerifier.create(personageDTOMono)
                .expectError(UndefinedPersonalityTypeException.class)
                .verify();
    }

    @Test
    public void testUpdatePersonage(){
        String id = "d6964805";
        Personage personage = PersonageMapper.toEntity(this.arya);
        PersonageDTO personageDTO = this.arya;
        personageDTO.setAbout(personageDTO.getAbout()+". Updated personage!");
        Mockito.when(this.repository.findById(id)).thenReturn(Mono.just(personage));
        Mockito.when(this.repository.save(PersonageMapper.toEntity(personageDTO)))
                .thenReturn(Mono.just(PersonageMapper.toEntity(personageDTO)));

        Mono<PersonageDTO> personageDTOMono = this.service.updatePersonage(this.arya);

        StepVerifier.create(personageDTOMono)
                .expectNextMatches(dto -> {
                    return Objects.equals(dto.getId(), id) &&
                            Objects.equals(dto.getAbout(), "Third Personality, GOT. Updated personage!") &&
                            Objects.equals(dto.getPersonalityType(), "INFJ_T");
                })
                .verifyComplete();
    }

    @Test
    public void testUpdatePersonageWithUndefined(){
        String id = "e00fe410";
        PersonageDTO personageDTO = this.luffy;
        personageDTO.setPersonalityType("INTROVERSION-WITH-ERROR");
        Mono<PersonageDTO> personageDTOMono = this.service.updatePersonage(personageDTO);

        StepVerifier.create(personageDTOMono)
                .expectError(UndefinedPersonalityTypeException.class)
                .verify();
    }
}
