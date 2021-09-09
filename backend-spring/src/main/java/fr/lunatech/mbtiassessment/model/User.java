package fr.lunatech.mbtiassessment.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.lunatech.mbtiassessment.security.domain.UserPrincipal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Document("users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true, builderClassName = "Builder")
@JsonIgnoreProperties(value = {"password"}, allowSetters = true)
public class User implements Serializable {
    @Id
    private String id;
    private String lastName;
    private String firstName;
    private String email;
    private String username;
    @JsonIgnoreProperties("password")
    private String password;
    private String profileImageUrl;
    private Date lastLoginDate;
    private Date lastLoginDateDisplay;
    private Date joinDate;
    private boolean isEnabled;
    private boolean isNonLocked;
    private String role;
    private List<String> authorities;
    private List<PersonalityAssessment> personalityAssessments;

    public UserDetails toDetails() {
        return new UserPrincipal(this);
    }
}
