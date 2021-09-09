package fr.lunatech.mbtiassessment.security.domain;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static fr.lunatech.mbtiassessment.security.domain.Authority.*;

public enum Role {
    ROLE_USER("USER", USER_AUTHORITIES),
    ROLE_ADMIN("ADMIN", ADMIN_AUTHORITIES),
    ROLE_SUPER_USER("SUPER_ADMIN", SUPER_ADMIN_AUTHORITIES);

    private final String label;
    private final String[] authorities;

    private static final Map<String, Role> BY_LABEL = new HashMap<>();

    static {
        for (Role role : values()) {
            BY_LABEL.put(role.label, role);
        }
    }

    Role(String label, String... authorities) {
        this.label = label;
        this.authorities = authorities;
    }

    public String getLabel() {
        return this.label;
    }

    public String[] getAuthorities() {
        return authorities;
    }


    public static Role getRoleByString(String label) {
        label = label.toUpperCase(Locale.ROOT).trim();
        Role role = BY_LABEL.get(label);
        return role != null ? role : Role.ROLE_USER;
    }
}
