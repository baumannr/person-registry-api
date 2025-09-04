package hu.baumannr.personregistryapi.mapper;

import hu.baumannr.personregistryapi.persistence.model.Person;
import hu.baumannr.personregistryapi.rest.model.PersonCreateRequest;
import hu.baumannr.personregistryapi.rest.model.PersonResponse;
import hu.baumannr.personregistryapi.rest.model.PersonUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

/**
 * Mapper methods for person and related request objects.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PersonMapper {

    /**
     * Converts the given Person entity into a PersonResponse DTO.
     *
     * @param person the Person entity to convert
     * @return the PersonResponse DTO
     */
    @Mapping(target = "addresses", ignore = true)
    @Mapping(target = "contactInfos", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    PersonResponse convert(Person person);

    /**
     * Converts the given PersonCreateRequest DTO into a Person entity.
     *
     * @param request the PersonCreateRequest DTO to convert
     * @return the Person entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "addresses", ignore = true)
    @Mapping(target = "contactInfos", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    Person convert(PersonCreateRequest request);

    /**
     *
     * @param person the Person to update
     * @param request the PersonUpdateRequest with the new data for Person
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "addresses", ignore = true)
    @Mapping(target = "contactInfos", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updatePerson(@MappingTarget Person person, PersonUpdateRequest request);
}
