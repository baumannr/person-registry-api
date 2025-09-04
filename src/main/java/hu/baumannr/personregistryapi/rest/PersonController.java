package hu.baumannr.personregistryapi.rest;

import hu.baumannr.personregistryapi.rest.model.PersonCreateRequest;
import hu.baumannr.personregistryapi.rest.model.PersonResponse;
import hu.baumannr.personregistryapi.rest.model.PersonUpdateRequest;
import hu.baumannr.personregistryapi.service.PersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

/**
 * REST controller for managing persons.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class PersonController implements PersonApi {

    private final PersonService personService;

    @Override
    public ResponseEntity<PersonResponse> getPersonById(Long personId) {
        log.info("GET /persons/{personId} called, personId: {}", personId);
        PersonResponse person = personService.getPersonById(personId);
        return ResponseEntity.ok(person);
    }

    @Override
    public ResponseEntity<List<PersonResponse>> getAllPersons() {
        log.info("GET /persons called");
        List<PersonResponse> persons = personService.getAllPersons();
        return ResponseEntity.ok(persons);
    }

    @Override
    public ResponseEntity<PersonResponse> createPerson(PersonCreateRequest personCreateRequest) {
        log.info("POST /persons called");
        PersonResponse response = personService.createPerson(personCreateRequest);
        log.info("Person created, ID: {}", response.getId());
        URI location = URI.create("/api/persons/" + response.getId());
        return ResponseEntity.created(location)
                .body(response);
    }

    @Override
    public ResponseEntity<PersonResponse> updatePerson(Long personId, PersonUpdateRequest personUpdateRequest) {
        log.info("PUT /persons/{personId} called, person ID: {}", personId);
        PersonResponse response = personService.updatePerson(personId, personUpdateRequest);
        log.info("Person updated, ID: {}", response.getId());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> deletePerson(Long personId) {
        log.info("DELETE /persons/{personId} called, person ID: {}", personId);
        personService.deletePerson(personId);
        log.info("Person deleted, ID: {}", personId);
        return ResponseEntity.noContent().build();
    }
}
