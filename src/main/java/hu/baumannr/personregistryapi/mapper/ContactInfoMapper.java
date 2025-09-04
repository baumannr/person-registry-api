package hu.baumannr.personregistryapi.mapper;

import hu.baumannr.personregistryapi.persistence.model.ContactInfo;
import hu.baumannr.personregistryapi.rest.model.ContactInfoCreateRequest;
import hu.baumannr.personregistryapi.rest.model.ContactInfoResponse;
import hu.baumannr.personregistryapi.rest.model.ContactInfoUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

/**
 * Mapper methods for contact information and related request objects.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ContactInfoMapper {

    /**
     * Converts the given ContactInfo entity into a ContactInfoResponse DTO.
     *
     * @param contactInfo the ContactInfo entity to convert
     * @return the ContactInfoResponse DTO
     */
    ContactInfoResponse convert(ContactInfo contactInfo);

    /**
     * Converts the given ContactInfoCreateRequest DTO into a ContactInfo entity.
     *
     * @param request the ContactInfoCreateRequest DTO to convert
     * @return the ContactInfo entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "person", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ContactInfo convert(ContactInfoCreateRequest request);

    /**
     * Updates the given ContactInfo with the ContactInfoUpdateRequest DTO.
     *
     * @param contactInfo the ContactInfo to update
     * @param request the ContactInfoUpdateRequest with the new data for ContactInfo
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "person", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateContactInfo(@MappingTarget ContactInfo contactInfo, ContactInfoUpdateRequest request);
}
