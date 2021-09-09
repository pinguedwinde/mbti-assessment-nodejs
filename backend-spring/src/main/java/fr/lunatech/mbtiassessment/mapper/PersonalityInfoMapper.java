package fr.lunatech.mbtiassessment.mapper;

import fr.lunatech.mbtiassessment.dto.PersonalityInfoDTO;
import fr.lunatech.mbtiassessment.model.PersonalityInfo;
import fr.lunatech.mbtiassessment.model.util.PersonalityType;

import java.util.ArrayList;

public class PersonalityInfoMapper {

    public static PersonalityInfo toEntity(PersonalityInfoDTO personalityInfoDTO) {
        PersonalityType personalityType = PersonalityType.getPersonalityTypeByString(personalityInfoDTO.getPersonalityType());
        PersonalityInfo personalityInfo = new PersonalityInfo();
        personalityInfo.setId(personalityInfoDTO.getId());
        personalityInfo.setPersonalityType(personalityType);
        personalityInfo.setProfile(personalityInfoDTO.getProfile());
        personalityInfo.setGroup(personalityInfoDTO.getGroup());
        personalityInfo.setDescription(personalityInfoDTO.getDescription());
        return personalityInfo;
    }

    public static PersonalityInfoDTO toDto(PersonalityInfo personalityInfo) {
        PersonalityInfoDTO personalityInfoDTO = new PersonalityInfoDTO();
        personalityInfoDTO.setId(personalityInfo.getId());
        personalityInfoDTO.setPersonalityType(personalityInfo.getPersonalityType().name());
        personalityInfoDTO.setProfile(personalityInfo.getProfile());
        personalityInfoDTO.setGroup(personalityInfo.getGroup());
        personalityInfoDTO.setDescription(personalityInfo.getDescription());
        personalityInfoDTO.setPersonageDTOs(new ArrayList<>());
        return personalityInfoDTO;
    }
}
