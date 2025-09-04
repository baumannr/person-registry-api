package hu.baumannr.personregistryapi.rest;

import hu.baumannr.personregistryapi.rest.model.AddressCreateRequest;
import hu.baumannr.personregistryapi.rest.model.AddressResponse;
import hu.baumannr.personregistryapi.rest.model.AddressUpdateRequest;
import hu.baumannr.personregistryapi.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

/**
 * REST controller for managing addresses.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class AddressController implements AddressApi {

    private final AddressService addressService;

    @Override
    public ResponseEntity<List<AddressResponse>> getAddressesForPerson(Long personId) {
        log.info("GET /persons/{personId}/addresses called, personId: {}", personId);
        List<AddressResponse> response = addressService.getAddressesForPerson(personId);
        log.info("{} addresses found for person, personId: {}", response.size(), personId);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<AddressResponse> addAddress(Long personId, AddressCreateRequest addressCreateRequest) {
        log.info("POST /persons/{personId}/addresses called, personId: {}", personId);
        AddressResponse response = addressService.addAddress(personId, addressCreateRequest);
        log.info("Address created, addressId: {}, personId: {}", response.getId(), personId);
        URI location = URI.create("/persons/" + personId + "/addresses/" + response.getId());
        return ResponseEntity.created(location)
                .body(response);
    }

    @Override
    public ResponseEntity<AddressResponse> updateAddress(Long personId,
                                                         Long addressId,
                                                         AddressUpdateRequest addressUpdateRequest) {
        log.info("PUT /persons/{personId}/addresses/{addressId}");
        AddressResponse response = addressService.updateAddress(personId, addressId, addressUpdateRequest);
        log.info("Address updated, addressId: {}, personId: {}", addressId, personId);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> deleteAddress(Long personId, Long addressId) {
        log.info("DELETE /persons/{personId}/addresses/{addressId}");
        addressService.deleteAddress(personId, addressId);
        log.info("Address deleted, addressId: {}, personId: {}", addressId, personId);
        return ResponseEntity.noContent().build();
    }
}
