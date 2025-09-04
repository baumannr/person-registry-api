package hu.baumannr.personregistryapi.rest;

import hu.baumannr.personregistryapi.rest.model.ContactInfoCreateRequest;
import hu.baumannr.personregistryapi.rest.model.ContactInfoResponse;
import hu.baumannr.personregistryapi.rest.model.ContactInfoUpdateRequest;
import hu.baumannr.personregistryapi.service.ContactInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

/**
 * REST controller for managing contact information.
 */
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ContactInfoController implements ContactInfoApi {

    private final ContactInfoService contactInfoService;

    @Override
    public ResponseEntity<List<ContactInfoResponse>> getContactInfosForPerson(Long personId) {
        log.info("GET /persons/{personId}/contactInfos called, personId: {}", personId);
        List<ContactInfoResponse> response = contactInfoService.getContactInfosForPerson(personId);
        log.info("{} contact information found for person, personId: {}", response.size(), personId);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ContactInfoResponse> addContactInfo(Long personId,
                                                              ContactInfoCreateRequest contactInfoCreateRequest) {
        log.info("POST /persons/{personId}/contactInfos called, personId: {}", personId);
        ContactInfoResponse response = contactInfoService.addContactInfo(personId, contactInfoCreateRequest);
        log.info("Contact information created, contactInfoId: {}, personId: {}", response.getId(), personId);
        URI location = URI.create("/persons/" + personId + "/contactInfos/" + response.getId());
        return ResponseEntity.created(location)
                .body(response);
    }

    @Override
    public ResponseEntity<ContactInfoResponse> updateContactInfo(Long personId,
                                                                 Long contactInfoId,
                                                                 ContactInfoUpdateRequest request) {
        log.info("PUT /persons/{personId}/contactInfos/{contactInfoId}");
        ContactInfoResponse response = contactInfoService.updateContactInfo(personId, contactInfoId, request);
        log.info("Contact information updated, contactInfoId: {}, personId: {}", contactInfoId, personId);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> deleteContactInfo(Long personId, Long contactInfoId) {
        log.info("DELETE /persons/{personId}/contactInfos/{contactInfoId}");
        contactInfoService.deleteContactInfo(personId, contactInfoId);
        log.info("Contact information deleted, contactInfoId: {}, personId: {}", contactInfoId, personId);
        return ResponseEntity.noContent().build();
    }
}
