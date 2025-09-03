package hu.baumannr.personregistryapi.service;

import hu.baumannr.personregistryapi.rest.model.ContactInfoCreateRequest;
import hu.baumannr.personregistryapi.rest.model.ContactInfoResponse;
import hu.baumannr.personregistryapi.rest.model.ContactInfoUpdateRequest;

import java.util.List;

/**
 * Service interface for managing contact infos.
 */
public interface ContactInfoService {

    /**
     * Retrieves all contact information for the person identified by the given ID.
     *
     * @param personId the ID of the person
     * @return a list of the person's contact information
     */
    List<ContactInfoResponse> getContactInfosForPerson(Long personId);

    /**
     * Adds a new contact information entry for the person identified by the given ID.
     *
     * @param personId the ID of the person
     * @param request the data for the new contact information
     * @return the created contact information
     */
    ContactInfoResponse addContactInfo(Long personId, ContactInfoCreateRequest request);

    /**
     * Updates the contact information identified by the given ID with the provided data.
     *
     * @param personId the ID of the person
     * @param contactInfoId the ID of the contact information to update
     * @param request the new data for the contact information
     * @return the updated contact information
     */
    ContactInfoResponse updateContactInfo(Long personId, Long contactInfoId, ContactInfoUpdateRequest request);

    /**
     * Deletes the contact information identified by the given ID.
     *
     * @param personId the ID of the person
     * @param contactInfoId the ID of the contact information to delete
     */
    void deleteContactInfo(Long personId, Long contactInfoId);
}
