package fr.lunatech.mbtiassessment.mapper;

import fr.lunatech.mbtiassessment.dto.PersonageDTO;
import fr.lunatech.mbtiassessment.model.Personage;
import fr.lunatech.mbtiassessment.model.util.PersonalityType;

import java.util.List;
import java.util.stream.Collectors;

public class PersonageMapper {

    public static Personage toEntity(PersonageDTO personageDTO) {
        PersonalityType personalityType = PersonalityType.getPersonalityTypeByString(personageDTO.getPersonalityType());
        Personage personage = new Personage();
        personage.setId(personageDTO.getId());
        personage.setName(personageDTO.getName());
        personage.setAbout(personageDTO.getAbout());
        personage.setPersonalityType(personalityType);
        personage.setUniverse(personageDTO.getUniverse());
        return personage;
    }

    public static List<Personage> toEntities(List<PersonageDTO> personageDTOs) {
        return personageDTOs.stream().map(PersonageMapper::toEntity)
                .collect(Collectors.toList());
    }

    public static PersonageDTO toDto(Personage personage) {
        PersonageDTO personageDTO = new PersonageDTO();
        personageDTO.setId(personage.getId());
        personageDTO.setName(personage.getName());
        personageDTO.setAbout(personage.getAbout());
        personageDTO.setPersonalityType(personage.getPersonalityType().name());
        personageDTO.setUniverse(personage.getUniverse());
        return personageDTO;
    }

    public static List<PersonageDTO> toDtos(List<Personage> personages) {
        return personages.stream().map(PersonageMapper::toDto)
                .collect(Collectors.toList());
    }
}
