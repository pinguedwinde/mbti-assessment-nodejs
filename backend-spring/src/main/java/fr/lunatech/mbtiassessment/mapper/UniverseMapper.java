package fr.lunatech.mbtiassessment.mapper;

import fr.lunatech.mbtiassessment.dto.UniverseDTO;
import fr.lunatech.mbtiassessment.model.util.Universe;

public class UniverseMapper {



    public static Universe toEntity(UniverseDTO universeDTO){
        Universe universe = Universe.getUniverseByString(universeDTO.getTitle());
        return universe;
    }

    public static UniverseDTO toDto(Universe universe) {
        UniverseDTO universeDTO = new UniverseDTO(universe.title);
        return universeDTO;
    }
}
