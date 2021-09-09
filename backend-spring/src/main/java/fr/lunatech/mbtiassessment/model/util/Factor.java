package fr.lunatech.mbtiassessment.model.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public enum Factor {
    EXTRAVERSION("extraversion"), INTROVERSION("introversion"),
    INTUITION("intuition"), SENSING("sensing"),
    FEELING("feeling"), THINKING("thinking"),
    JUDGING("judging"), PERCEIVING("perceiving"),
    ASSERTIVE("assertive"), TURBULENT("turbulent"),
    UNDEFINED("undefined");

    public final String label;
    private static final Map<String, Factor> BY_LABEL = new HashMap<>();

    static {
        for (Factor factor : values()) {
            BY_LABEL.put(factor.label, factor);
        }
    }

    Factor(String label) {
        this.label = label;
    }

    public static Factor getFactorByString(String label) {
        label = label.toLowerCase(Locale.ROOT).trim();
        Factor factor = BY_LABEL.get(label);
        return factor != null ? factor : Factor.UNDEFINED;
    }
}
