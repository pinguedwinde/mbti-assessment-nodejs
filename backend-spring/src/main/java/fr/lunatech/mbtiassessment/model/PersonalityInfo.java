package fr.lunatech.mbtiassessment.model;

import fr.lunatech.mbtiassessment.model.util.PersonalityType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document("about_personalities")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonalityInfo implements Serializable {
    @Id
    private String id;
    private PersonalityType personalityType;
    private String profile;
    private String group;
    private String description;
}
