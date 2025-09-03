package hu.baumannr.personregistryapi.service;

import hu.baumannr.personregistryapi.rest.model.PersonCreateRequest;
import hu.baumannr.personregistryapi.rest.model.PersonResponse;
import hu.baumannr.personregistryapi.rest.model.PersonUpdateRequest;

import java.util.List;

/**
 * Service interface for managing persons.
 */
public interface PersonService {

    /**
     * Retrieves a person by their id.
     *
     * @param personId the ID of the person
     * @return the person data
     */
    PersonResponse getPersonById(Long personId);

    /**
     * Retrieves all persons.
     *
     * @return list of all persons
     */
    List<PersonResponse> getAllPerson();

    /**
     * Creates a new person using the provided request data.
     *
     * @param request the data for the new person
     * @return the created person
     */
    PersonResponse createPerson(PersonCreateRequest request);

    /**
     * Updates the person identified by the given ID with the provided data.
     *
     * @param personId the ID of the person to update
     * @param request the new data for the person
     * @return the updated person
     */
    PersonResponse updatePerson(Long personId, PersonUpdateRequest request);

    /**
     * Deletes the person identified by the given ID.
     *
     * @param personId the ID of the person to delete
     */
    void deletePerson(Long personId);
}
