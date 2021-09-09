package fr.lunatech.mbtiassessment.model;

import fr.lunatech.mbtiassessment.model.util.Factor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document("questions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question implements Serializable {
    @Id
    private String id;
    private String content;
    @Indexed
    private Factor factor;
}
