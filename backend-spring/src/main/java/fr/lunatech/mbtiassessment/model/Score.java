package fr.lunatech.mbtiassessment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Score implements Serializable {
    private int extraversion;
    private int intuition;
    private int feeling;
    private int judging;
    private int assertive;
}
