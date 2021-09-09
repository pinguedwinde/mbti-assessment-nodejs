package fr.lunatech.mbtiassessment.model;

import fr.lunatech.mbtiassessment.model.util.PersonalityType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document("personages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Personage implements Serializable {

    @Id
    private String id;
    private String name;
    private String about;
    private PersonalityType personalityType;
    private String universe;

}
