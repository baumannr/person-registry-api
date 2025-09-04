package hu.baumannr.personregistryapi.mapper;

import hu.baumannr.personregistryapi.persistence.model.Address;
import hu.baumannr.personregistryapi.rest.model.AddressCreateRequest;
import hu.baumannr.personregistryapi.rest.model.AddressResponse;
import hu.baumannr.personregistryapi.rest.model.AddressUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

/**
 * Mapper methods for address and related request objects.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AddressMapper {

    /**
     * Converts the given Address entity into a AddressResponse DTO.
     *
     * @param address the Address entity to convert
     * @return the AddressResponse DTO
     */
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    AddressResponse convert(Address address);

    /**
     * Converts the given AddressCreateRequest DTO into an Address entity.
     *
     * @param request the AddressCreateRequest DTO to convert
     * @return the Address entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "person", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Address convert(AddressCreateRequest request);

    /**
     * Updates the given Address with the AddressUpdateRequest DTO.
     *
     * @param address the Address to update
     * @param request the AddressUpdateRequest with the new data for Address
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "person", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateAddress(@MappingTarget Address address, AddressUpdateRequest request);
}
