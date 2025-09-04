package hu.baumannr.personregistryapi.service;

import hu.baumannr.personregistryapi.exception.PersonApiException;
import hu.baumannr.personregistryapi.mapper.PersonMapper;
import hu.baumannr.personregistryapi.persistence.model.Person;
import hu.baumannr.personregistryapi.persistence.repository.PersonRepository;
import hu.baumannr.personregistryapi.rest.model.PersonCreateRequest;
import hu.baumannr.personregistryapi.rest.model.PersonResponse;
import hu.baumannr.personregistryapi.rest.model.PersonUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    private final PersonMapper personMapper;

    @Override
    public PersonResponse getPersonById(Long personId) {
        Person person = getPerson(personId);
        return personMapper.convert(person);
    }

    @Override
    public List<PersonResponse> getAllPersons() {
        List<Person> persons = personRepository.findAll();
        return persons.stream()
                .map(personMapper::convert)
                .toList();
    }

    @Override
    public PersonResponse createPerson(PersonCreateRequest request) {
        Person person = personMapper.convert(request);
        Person savedPerson = personRepository.save(person);
        return personMapper.convert(savedPerson);
    }

    @Override
    public PersonResponse updatePerson(Long personId, PersonUpdateRequest request) {
        Person person = getPerson(personId);
        personMapper.updatePerson(person, request);
        Person updatedPerson = personRepository.save(person);
        return personMapper.convert(updatedPerson);
    }

    @Override
    public void deletePerson(Long personId) {
        personRepository.deleteById(personId);
    }

    private Person getPerson(Long personId) {
        return personRepository.findById(personId)
                .orElseThrow(() -> new PersonApiException(HttpStatus.NOT_FOUND, "Person not found!"));
    }
}
