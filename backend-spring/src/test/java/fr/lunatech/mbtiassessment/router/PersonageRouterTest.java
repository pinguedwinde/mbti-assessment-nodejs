package fr.lunatech.mbtiassessment.router;

import fr.lunatech.mbtiassessment.dto.PersonageDTO;
import fr.lunatech.mbtiassessment.dto.UniverseDTO;
import fr.lunatech.mbtiassessment.error.domain.UndefinedPersonalityTypeException;
import fr.lunatech.mbtiassessment.service.PersonageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static fr.lunatech.mbtiassessment.router.RoutesConstants.PERSONAGE_BASE_URL;
import static fr.lunatech.mbtiassessment.security.validator.ValidatorsConstants.PERSONALITY_TYPE_PATTERN;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PersonageRouterTest {

    private WebTestClient client;
    @MockBean
    private PersonageService service;
    @LocalServerPort
    private int port;

    private List<PersonageDTO> personageDTOS;
    private PersonageDTO tanya;
    private PersonageDTO luffy;
    private PersonageDTO arya;
    private final String undefinedPersonalityType;
    private final String notFoundMessage;

    private UniverseDTO [] universeDTOS;


    public PersonageRouterTest() {
        this.undefinedPersonalityType = "Undefined Personality Type";
        this.notFoundMessage = "Personage not found cause by either bad path or invalid ID";
    }

    @BeforeEach
    public void setup (){
        this.client = WebTestClient
                .bindToServer()
                .baseUrl("http://localhost:" + port + PERSONAGE_BASE_URL)
                .build();
        this.tanya = new PersonageDTO("78c3c426","Miss Tanya", "First Personality, She is Singer AfroTrap Diva", "INTJ_A","Afro Trap Burkina");
        this.luffy = new PersonageDTO("e00fe410","Monkey D Luffy", "Second Personality, kaizoku oni ore wa naruto : of One Piece", "ISTP_T","One Piece");
        this.arya = new PersonageDTO("d6964805", "Arya Stark", "Third Personality, GOT", "INFJ_T","GOT");
        this.personageDTOS = new ArrayList<>();
        this.personageDTOS.add(this.tanya);
        this.personageDTOS.add(this.arya);
        this.personageDTOS.add(this.luffy);
        this.personageDTOS.add(new PersonageDTO("4f729859", "James St Patrick the Ghost", "Fouth Personality, Power", "INFJ_T","Power"));


        universeDTOS = new UniverseDTO[3];
        universeDTOS[0] =  new UniverseDTO("MARVEL");
        universeDTOS[1] = new UniverseDTO("GOT");
        universeDTOS[2] = new UniverseDTO("MATRIX");

    }

    @Test
    public void testGetAllPersonages(){
        Mockito.when(this.service.getAllPersonages()).thenReturn(Flux.fromIterable(personageDTOS));
        this.client
                .get()
                .uri("")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(PersonageDTO.class)
                .isEqualTo(personageDTOS)
                .hasSize(4);
    }
    @Test
    public void testGetAllUniverse(){
        Mockito.when(this.service.getAllUniverse()).thenReturn(Flux.fromArray(universeDTOS));
        this.client
                .get()
                .uri("/universe")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(UniverseDTO.class)
                .isEqualTo(Arrays.stream(universeDTOS).toList())
                .hasSize(3);
    }

    @Test
    public void testGetPersonageById(){
        String id = "78c3c426";
        Mockito.when(this.service.getPersonageById(id)).thenReturn(Mono.just(this.tanya));
        this.client
                .get()
                .uri("/id/{id}", id)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(PersonageDTO.class)
                .isEqualTo(this.tanya);
    }

    @Test
    public void testReturnJsonWhenGetPersonageById(){
        String id = "78c3c426";
        Mockito.when(this.service.getPersonageById(id)).thenReturn(Mono.just(this.tanya));
        this.client
                .get()
                .uri("/id/{id}", id)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(this.tanya.getId())
                .jsonPath("$.name").isEqualTo(this.tanya.getName())
                .jsonPath("$.about").isEqualTo(this.tanya.getAbout())
                .jsonPath("$.personalityType").isEqualTo(this.tanya.getPersonalityType())
                .jsonPath("$.universe").isEqualTo(this.tanya.getUniverse());
    }

    @Test
    public void testGetPersonageByIdWithInvalidIdNotFound(){
        String id = "78c3c426-error";
        Mockito.when(this.service.getPersonageById(id)).thenReturn(Mono.empty());
        this.client
                .get()
                .uri("/id/{id}", id)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    public void testGetPersonageByPersonalityType(){
        Mockito.when(this.service.getPersonagesByPersonalityType("INFJ_T"))
                .thenReturn(Flux.just(this.arya, this.personageDTOS.get(3)));
        this.client
                .get()
                .uri("/personality-type/{personality-type}", "INFJ_T")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo(this.arya.getId())
                .jsonPath("$[0].name").isEqualTo(this.arya.getName())
                .jsonPath("$[0].about").isEqualTo(this.arya.getAbout())
                .jsonPath("$[0].personalityType").isEqualTo(this.arya.getPersonalityType())
                .jsonPath("$[0].universe").isEqualTo(this.arya.getUniverse())
                .jsonPath("$[1].personalityType").isEqualTo(this.personageDTOS.get(3).getPersonalityType())
                .jsonPath("$[1].name").isEqualTo(personageDTOS.get(3).getName());
    }
    @Test
    public void testGetPersonageByUniverse(){
        Mockito.when(this.service.getPersonagesByUniverse("GOT"))
                .thenReturn(Flux.just(this.arya, this.personageDTOS.get(3)));
        this.client
                .get()
                .uri("/universe/{universe}", "GOT")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo(this.arya.getId())
                .jsonPath("$[0].name").isEqualTo(this.arya.getName())
                .jsonPath("$[0].about").isEqualTo(this.arya.getAbout())
                .jsonPath("$[0].personalityType").isEqualTo(this.arya.getPersonalityType())
                .jsonPath("$[0].universe").isEqualTo(this.arya.getUniverse())
                .jsonPath("$[1].universe").isEqualTo(this.personageDTOS.get(3).getUniverse())
                .jsonPath("$[1].name").isEqualTo(personageDTOS.get(3).getName());
    }
    @Test
    public void testGetPersonageByTypeAndUniverse(){
        Mockito.when(this.service.getPersonageByTypeAndUniverse("INFJ_T","GOT"))
                .thenReturn(Flux.just(this.arya, this.personageDTOS.get(3)));
        this.client
                .get()
                .uri("/all/{personalityType}/{universe}", "INFJ_T","GOT")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo(this.arya.getId())
                .jsonPath("$[0].name").isEqualTo(this.arya.getName())
                .jsonPath("$[0].about").isEqualTo(this.arya.getAbout())
                .jsonPath("$[0].personalityType").isEqualTo(this.arya.getPersonalityType())
                .jsonPath("$[0].universe").isEqualTo(this.arya.getUniverse())
                .jsonPath("$[1].personalityType").isEqualTo(this.personageDTOS.get(3).getPersonalityType())
                .jsonPath("$[1].universe").isEqualTo(this.personageDTOS.get(3).getUniverse())
                .jsonPath("$[1].name").isEqualTo(personageDTOS.get(3).getName());

    }

    @Test
    public void testGetPersonageByUndefinedPersonalityType(){
        Mockito.when(this.service.getPersonagesByPersonalityType("ISTP_T-with-error"))
                .thenReturn(Flux.error(UndefinedPersonalityTypeException::new));
        this.client
                .get()
                .uri("/personality-type/{personality-type}", "ISTP_T-with-error")
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody()
                .jsonPath("$.path").isEqualTo(PERSONAGE_BASE_URL + "/personality-type/ISTP_T-with-error")
                .jsonPath("$.status").isEqualTo("BAD_REQUEST")
                .jsonPath("$.code").isEqualTo("400")
                .jsonPath("$.message").isEqualTo(undefinedPersonalityType);
    }

    @Test
    public void testCreatePersonage(){
        String id = "6081546c2209146d12bb0d19";
        PersonageDTO personageDTO = new PersonageDTO(this.luffy.getName(), this.luffy.getAbout(), this.luffy.getPersonalityType(),this.luffy.getUniverse());
        Mockito.when(this.service.addPersonage(personageDTO))
                .thenReturn(Mono.just(personageDTO).flatMap(personageDTO1 -> {
                    personageDTO1.setId(id);
                    return Mono.just(personageDTO1);
                }));
        this.client
                .post()
                .uri("")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(this.luffy))
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo(id)
                .jsonPath("$.name").isEqualTo(personageDTO.getName())
                .jsonPath("$.about").isEqualTo(personageDTO.getAbout())
                .jsonPath("$.personalityType").isEqualTo(personageDTO.getPersonalityType())
                .jsonPath("$.universe").isEqualTo(personageDTO.getUniverse());
    }

    @Test
    public void testCreatePersonageWithUndefinedFactor(){
        PersonageDTO personageDTO = new PersonageDTO(this.luffy.getName(), this.luffy.getAbout(), this.luffy.getPersonalityType(),this.luffy.getUniverse());
        Mockito.when(this.service.addPersonage(personageDTO)).thenReturn(Mono.error(UndefinedPersonalityTypeException::new));
        this.client
                .post()
                .uri("")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(personageDTO))
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody()
                .jsonPath("$.path").isEqualTo(PERSONAGE_BASE_URL)
                .jsonPath("$.status").isEqualTo("BAD_REQUEST")
                .jsonPath("$.code").isEqualTo("400")
                .jsonPath("$.message").isEqualTo(undefinedPersonalityType);
    }

    @Test
    public void testUpdatePersonage(){
        PersonageDTO personageDTO = this.personageDTOS.get(3);
        Mockito.when(this.service.updatePersonage(personageDTO)).thenReturn(Mono.just(personageDTO).flatMap(
                personageDTO1 -> {
                    personageDTO1.setAbout(personageDTO1.getAbout()+". Updated.");
                    return Mono.just(personageDTO1);
                }
        ));
        this.client
                .put()
                .uri("/{id}", personageDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(personageDTO))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(personageDTO.getId())
                .jsonPath("$.name").isEqualTo(personageDTO.getName())
                .jsonPath("$.about").isEqualTo(personageDTO.getAbout())
                .jsonPath("$.personalityType").isEqualTo(personageDTO.getPersonalityType())
                .jsonPath("$.universe").isEqualTo(personageDTO.getUniverse());
    }

    @Test
    public void testUpdatePersonageWithUndefinedPersonalityType(){
        this.arya.setPersonalityType("ERROR");
        Mockito.when(this.service.updatePersonage(this.arya)).thenReturn(Mono.error(UndefinedPersonalityTypeException::new));
        this.client
                .put()
                .uri("/{id}", this.arya.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(this.arya))
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("BAD_REQUEST")
                .jsonPath("$.statusCode").isEqualTo("400")
                .jsonPath("$.messages").isEqualTo(PERSONALITY_TYPE_PATTERN);
    }

    @Test
    public void testUpdatePersonageInvalidIdNotFound(){
        this.luffy.setId("INVALID_ID");
        Mockito.when(this.service.updatePersonage(this.luffy)).thenReturn(Mono.empty());
        this.client
                .put()
                .uri("/{id}", this.luffy.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(this.luffy))
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody()
                .jsonPath("$.path").isEqualTo(PERSONAGE_BASE_URL + "/" + this.luffy.getId())
                .jsonPath("$.status").isEqualTo("NOT_FOUND")
                .jsonPath("$.code").isEqualTo("404")
                .jsonPath("$.message").isEqualTo(notFoundMessage);
    }

    @Test
    public void testDeletePersonage(){
        String id = "d6964805";
        Mockito.when(this.service.getPersonageById(id)).thenReturn(Mono.just(this.luffy));
        Mockito.when(this.service.deletePersonage(this.luffy)).thenReturn(Mono.empty());
        this.client
                .delete()
                .uri("/{id}", id)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .isEmpty();
    }

    @Test
    public void testDeletePersonageInvalidIdNotFound(){
        String id = "78c3c426-with-error";
        Mockito.when(this.service.getPersonageById(id)).thenReturn(Mono.empty());
        this.client
                .delete()
                .uri("/{id}", id)
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody()
                .jsonPath("$.path").isEqualTo(PERSONAGE_BASE_URL + "/" + id)
                .jsonPath("$.status").isEqualTo("NOT_FOUND")
                .jsonPath("$.code").isEqualTo("404")
                .jsonPath("$.message").isEqualTo(notFoundMessage);
    }
}
