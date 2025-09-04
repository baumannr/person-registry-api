package hu.baumannr.personregistryapi.service;

import hu.baumannr.personregistryapi.exception.PersonApiException;
import hu.baumannr.personregistryapi.mapper.PersonMapper;
import hu.baumannr.personregistryapi.persistence.model.Person;
import hu.baumannr.personregistryapi.persistence.repository.PersonRepository;
import hu.baumannr.personregistryapi.rest.model.PersonResponse;
import hu.baumannr.personregistryapi.rest.model.PersonUpdateRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonServiceImplTest {

    @InjectMocks
    private PersonServiceImpl personService;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private PersonMapper personMapper;

    @Test
    void getPersonById_HappyPath() {
        // Arrange
        Long personId = 17L;
        Person person = mock(Person.class);
        Optional<Person> personOptional = Optional.of(person);
        PersonResponse expected = mock(PersonResponse.class);

        when(personRepository.findById(any())).thenReturn(personOptional);
        when(personMapper.convert(any(Person.class))).thenReturn(expected);

        // Act
        PersonResponse actual = personService.getPersonById(personId);

        // Assert
        assertEquals(actual, expected);

        InOrder inOrder = inOrder(personRepository, personMapper);
        inOrder.verify(personRepository).findById(personId);
        inOrder.verify(personMapper).convert(person);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void getPersonById_WhenPersonNotFound_ThenThrowsException() {
        // Arrange
        Long personId = 17L;
        Optional<Person> personOptional = Optional.empty();
        LocalDateTime now = mock(LocalDateTime.class);

        when(personRepository.findById(any())).thenReturn(personOptional);

        try (MockedStatic<LocalDateTime> localDateTimeMockedStatic = Mockito.mockStatic(LocalDateTime.class)) {
            localDateTimeMockedStatic.when(LocalDateTime::now).thenReturn(now);

            // Act
            PersonApiException personApiException = assertThrows(PersonApiException.class,
                    () -> personService.getPersonById(personId));

            // Assert
            assertEquals(HttpStatus.NOT_FOUND, personApiException.getStatusCode());
            assertEquals("Person not found!", personApiException.getMessage());
            assertEquals(now, personApiException.getTimestamp());

            InOrder inOrder = inOrder(personRepository, personMapper);
            inOrder.verify(personRepository).findById(personId);
            inOrder.verifyNoMoreInteractions();
        }
    }

    @Test
    void getAllPersons_HappyPAth() {
        // Arrange
        Person person1 = mock(Person.class);
        Person person2 = mock(Person.class);
        List<Person> persons = List.of(person1, person2);

        PersonResponse personResponse1 = mock(PersonResponse.class);
        PersonResponse personResponse2 = mock(PersonResponse.class);
        List<PersonResponse> expected = List.of(personResponse1, personResponse2);

        when(personRepository.findAll()).thenReturn(persons);
        when(personMapper.convert(person1)).thenReturn(personResponse1);
        when(personMapper.convert(person2)).thenReturn(personResponse2);

        // Act
        List<PersonResponse> actual = personService.getAllPersons();

        // Assert
        assertIterableEquals(expected, actual);

        InOrder inOrder = inOrder(personRepository, personMapper);
        inOrder.verify(personRepository).findAll();
        inOrder.verify(personMapper).convert(person1);
        inOrder.verify(personMapper).convert(person2);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void createPerson_HappyPath() {
        // Arrange
        Long personId = 17L;
        Person person = mock(Person.class);
        Optional<Person> personOptional = Optional.of(person);
        PersonResponse expected = mock(PersonResponse.class);

        when(personRepository.findById(any())).thenReturn(personOptional);
        when(personMapper.convert(any(Person.class))).thenReturn(expected);

        // Act
        PersonResponse actual = personService.getPersonById(personId);

        // Assert
        assertEquals(actual, expected);

        InOrder inOrder = inOrder(personRepository, personMapper);
        inOrder.verify(personRepository).findById(personId);
        inOrder.verify(personMapper).convert(person);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void updatePerson_HappyPath() {
        // Arrange
        Long personId = 17L;
        PersonUpdateRequest request = mock(PersonUpdateRequest.class);
        Person person = mock(Person.class);
        Person updatedPerson = mock(Person.class);
        Optional<Person> personOptional = Optional.of(person);
        PersonResponse expected = mock(PersonResponse.class);

        when(personRepository.findById(any())).thenReturn(personOptional);
        when(personRepository.save(any())).thenReturn(updatedPerson);
        when(personMapper.convert(any(Person.class))).thenReturn(expected);

        // Act
        PersonResponse actual = personService.updatePerson(personId, request);

        // Assert
        assertEquals(actual, expected);

        InOrder inOrder = inOrder(personRepository, personMapper);
        inOrder.verify(personRepository).findById(personId);
        inOrder.verify(personMapper).updatePerson(person, request);
        inOrder.verify(personRepository).save(person);
        inOrder.verify(personMapper).convert(updatedPerson);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void updatePerson_WhenPersonNotFound_ThenThrowsException() {
        // Arrange
        Long personId = 17L;
        PersonUpdateRequest request = mock(PersonUpdateRequest.class);
        Optional<Person> personOptional = Optional.empty();
        LocalDateTime now = mock(LocalDateTime.class);

        when(personRepository.findById(any())).thenReturn(personOptional);

        try (MockedStatic<LocalDateTime> localDateTimeMockedStatic = Mockito.mockStatic(LocalDateTime.class)) {
            localDateTimeMockedStatic.when(LocalDateTime::now).thenReturn(now);

            // Act
            PersonApiException personApiException = assertThrows(PersonApiException.class,
                    () -> personService.updatePerson(personId, request));

            // Assert
            assertEquals(HttpStatus.NOT_FOUND, personApiException.getStatusCode());
            assertEquals("Person not found!", personApiException.getMessage());
            assertEquals(now, personApiException.getTimestamp());

            InOrder inOrder = inOrder(personRepository, personMapper);
            inOrder.verify(personRepository).findById(personId);
            inOrder.verifyNoMoreInteractions();
        }
    }

    @Test
    void deletePerson_HappyPath() {
        // Arrange
        Long personId = 17L;

        // Act
        personService.deletePerson(personId);

        // Assert
        verify(personRepository).deleteById(personId);
        verifyNoMoreInteractions(personRepository);
    }
}