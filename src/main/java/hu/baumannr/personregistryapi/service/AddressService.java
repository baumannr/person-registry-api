package hu.baumannr.personregistryapi.service;

import hu.baumannr.personregistryapi.rest.model.AddressCreateRequest;
import hu.baumannr.personregistryapi.rest.model.AddressResponse;
import hu.baumannr.personregistryapi.rest.model.AddressUpdateRequest;

import java.util.List;

/**
 * Service interface for managing addresses.
 */
public interface AddressService {

    /**
     * Retrieves all addresses for the person identified by the given ID.
     *
     * @param personId the ID of the person
     * @return a list of the person's addresses
     */
    List<AddressResponse> getAddressesForPerson(Long personId);

    /**
     * Adds a new address entry for the person identified by the given ID.
     *
     * @param personId the ID of the person
     * @param request the data for the new address
     * @return the created address
     */
    AddressResponse addAddress(Long personId, AddressCreateRequest request);

    /**
     * Updates the address identified by the given ID with the provided data.
     *
     * @param personId the ID of the person
     * @param addressId the ID of the address to update
     * @param request the new data for the address
     * @return the updated address
     */
    AddressResponse updateAddress(Long personId, Long addressId, AddressUpdateRequest request);

    /**
     * Deletes the address identified by the given ID.
     *
     * @param personId the ID of the person
     * @param addressId the ID of the address to delete
     */
    void deleteAddress(Long personId, Long addressId);
}
