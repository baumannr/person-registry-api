package hu.baumannr.personregistryapi.rest;

import hu.baumannr.personregistryapi.rest.model.PersonCreateRequest;
import hu.baumannr.personregistryapi.rest.model.PersonResponse;
import hu.baumannr.personregistryapi.rest.model.PersonUpdateRequest;
import hu.baumannr.personregistryapi.service.PersonServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonControllerTest {

    @InjectMocks
    private PersonController personController;

    @Mock
    private PersonServiceImpl personService;

    @Test
    void getPersonById_HappyPath() {
        // Arrange
        Long personId = 13L;
        PersonResponse response = mock(PersonResponse.class);

        when(personService.getPersonById(anyLong())).thenReturn(response);

        // Act
        ResponseEntity<PersonResponse> actual = personController.getPersonById(personId);

        // Assert
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(response, actual.getBody());

        verify(personService).getPersonById(personId);
        verifyNoMoreInteractions(personService);
    }

    @Test
    void getAllPersons_HappyPath() {
        // Arrange
        PersonResponse response1 = mock(PersonResponse.class);
        PersonResponse response2 = mock(PersonResponse.class);
        List<PersonResponse> responses = List.of(response1, response2);

        when(personService.getAllPersons()).thenReturn(responses);

        // Act
        ResponseEntity<List<PersonResponse>> actual = personController.getAllPersons();

        // Assert
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertIterableEquals(responses, actual.getBody());

        verify(personService).getAllPersons();
        verifyNoMoreInteractions(personService);
    }

    @Test
    void createPerson_HappyPath() {
        // Arrange
        long personId = 13L;
        String location = "/api/persons/" + personId;
        PersonCreateRequest request = mock(PersonCreateRequest.class);
        PersonResponse response = mock(PersonResponse.class);

        when(response.getId()).thenReturn(personId);
        when(personService.createPerson(any())).thenReturn(response);

        // Act
        ResponseEntity<PersonResponse> actual = personController.createPerson(request);

        // Assert
        assertEquals(HttpStatus.CREATED, actual.getStatusCode());
        assertEquals(response, actual.getBody());

        assertNotNull(actual.getHeaders().getLocation());
        assertNotNull(actual.getHeaders().getLocation().toString());
        assertEquals(location, actual.getHeaders().getLocation().toString());

        verify(personService).createPerson(request);
        verifyNoMoreInteractions(personService);
    }

    @Test
    void updatePerson_HappyPath() {
        // Arrange
        long personId = 13L;
        PersonUpdateRequest request = mock(PersonUpdateRequest.class);
        PersonResponse response = mock(PersonResponse.class);

        when(personService.updatePerson(personId, request)).thenReturn(response);

        // Act
        ResponseEntity<PersonResponse> actual = personController.updatePerson(personId, request);

        // Assert
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(response, actual.getBody());

        verify(personService).updatePerson(personId, request);
        verifyNoMoreInteractions(personService);
    }

    @Test
    void deletePerson_HappyPath() {
        // Arrange
        long personId = 13L;

        // Act
        ResponseEntity<Void> actual = personController.deletePerson(personId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, actual.getStatusCode());

        verify(personService).deletePerson(personId);
        verifyNoMoreInteractions(personService);
    }
}