package fr.lunatech.mbtiassessment.model.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public enum Universe {

    PIRATES("PIRATES"),MARVEL("MARVEL"),STARWARS("STARWARS"),
    HARRYPOTTER("HARRYPOTTER"),MATRIX("MATRIX"),NARUTO("NARUTO"),
    XMEN("XMEN"),GOT("GOT"),RINGS("RINGS"),
    HUNGER("HUNGER"),ARROW("ARROW"),HOBBIT("HOBBIT"),
    SOUTHPARK("SOUTHPARK"),BATMAN("BATMAN"),ONEPIECE("ONEPIECE"),
    REALPERSON("REALPERSON"),UNDEFINED("UNDEFINED");

    public final String title;

    public static final Map<String, Universe> BY_TITLE =  new HashMap<>();

    static {
        for (Universe universe : values()){
            BY_TITLE.put(universe.title, universe);
        }
    }

    Universe(String title) {
        this.title = title;
    }
    public static Universe getUniverseByString(String title){
        title = title.toUpperCase(Locale.ROOT).trim();
        Universe universe = BY_TITLE.get(title);
        return universe != null ? universe : Universe.UNDEFINED;
    }
}
