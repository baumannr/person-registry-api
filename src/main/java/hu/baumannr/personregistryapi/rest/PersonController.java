package hu.baumannr.personregistryapi.rest;

import hu.baumannr.personregistryapi.rest.model.PersonCreateRequest;
import hu.baumannr.personregistryapi.rest.model.PersonResponse;
import hu.baumannr.personregistryapi.rest.model.PersonUpdateRequest;
import hu.baumannr.personregistryapi.service.PersonService;
import lombok.RequiredArgsConstructor;
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
public class PersonController implements PersonApi {

    private final PersonService personService;

    @Override
    public ResponseEntity<PersonResponse> getPersonById(Long personId) {
        PersonResponse person = personService.getPersonById(personId);
        return ResponseEntity.ok(person);
    }

    @Override
    public ResponseEntity<List<PersonResponse>> getAllPersons() {
        List<PersonResponse> persons = personService.getAllPersons();
        return ResponseEntity.ok(persons);
    }

    @Override
    public ResponseEntity<PersonResponse> createPerson(PersonCreateRequest personCreateRequest) {
        PersonResponse response = personService.createPerson(personCreateRequest);
        URI location = URI.create("/api/persons/" + response.getId());
        return ResponseEntity.created(location)
                .body(response);
    }

    @Override
    public ResponseEntity<PersonResponse> updatePerson(Long personId, PersonUpdateRequest personUpdateRequest) {
        PersonResponse response = personService.updatePerson(personId, personUpdateRequest);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> deletePerson(Long personId) {
        personService.deletePerson(personId);
        return ResponseEntity.noContent().build();
    }
}
