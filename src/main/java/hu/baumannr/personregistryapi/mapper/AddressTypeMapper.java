package hu.baumannr.personregistryapi.mapper;

import hu.baumannr.personregistryapi.persistence.model.AddressType;
import hu.baumannr.personregistryapi.rest.model.AddressCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * Mapper methods for address type.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AddressTypeMapper {

    AddressType convert(AddressCreateRequest.TypeEnum type);
}
