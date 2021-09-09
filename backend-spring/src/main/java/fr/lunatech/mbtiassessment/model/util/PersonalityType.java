package fr.lunatech.mbtiassessment.model.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public enum PersonalityType {
    INTJ_A("INTJ_A"), INTP_A("INTP_A"), ENTJ_A("ENTJ_A"), ENTP_A("ENTP_A"),
    INTJ_T("INTJ_T"), INTP_T("INTP_T"), ENTJ_T("ENTJ_T"), ENTP_T("ENTP_T"),
    INFJ_A("INFJ_A"), INFP_A("INFP_A"), ENFJ_A("ENFJ_A"), ENFP_A("ENFP_A"),
    INFJ_T("INFJ_T"), INFP_T("INFP_T"), ENFJ_T("ENFJ_T"), ENFP_T("ENFP_T"),
    ISTJ_A("ISTJ_A"), ISFJ_A("ISFJ_A"), ESTJ_A("ESTJ_A"), ESFJ_A("ESFJ_A"),
    ISTJ_T("ISTJ_T"), ISFJ_T("ISFJ_T"), ESTJ_T("ESTJ_T"), ESFJ_T("ESFJ_T"),
    ISTP_A("ISTP_A"), ISFP_A("ISFP_A"), ESTP_A("ESTP_A"), ESFP_A("ESFP_A"),
    ISTP_T("ISTP_T"), ISFP_T("ISFP_T"), ESTP_T("ESTP_T"), ESFP_T("ESFP_T"),
    UNDEFINED("UNDEFINED");

    public final String label;

    public static final Map<String, PersonalityType> BY_LABEL = new HashMap<>();

    static {
        for (PersonalityType personalityType : values()) {
            BY_LABEL.put(personalityType.label, personalityType);
        }
    }

    PersonalityType(String label) {
        this.label = label;
    }

    public static PersonalityType getPersonalityTypeByString(String label) {
        label = label.toUpperCase(Locale.ROOT).trim();
        PersonalityType personalityType = BY_LABEL.get(label);
        return personalityType != null ? personalityType : PersonalityType.UNDEFINED;
    }
}
