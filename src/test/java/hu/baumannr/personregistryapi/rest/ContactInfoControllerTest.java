package hu.baumannr.personregistryapi.rest;

import hu.baumannr.personregistryapi.rest.model.ContactInfoCreateRequest;
import hu.baumannr.personregistryapi.rest.model.ContactInfoResponse;
import hu.baumannr.personregistryapi.rest.model.ContactInfoUpdateRequest;
import hu.baumannr.personregistryapi.service.ContactInfoService;
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
class ContactInfoControllerTest {

    @InjectMocks
    private ContactInfoController contactInfoController;
    
    @Mock
    private ContactInfoService contactInfoService;

    @Test
    void getContactInfosForPerson_HappyPath() {
        // Arrange
        Long personId = 13L;
        ContactInfoResponse response1 = mock(ContactInfoResponse.class);
        ContactInfoResponse response2 = mock(ContactInfoResponse.class);
        List<ContactInfoResponse> response = List.of(response1, response2);

        when(contactInfoService.getContactInfosForPerson(anyLong())).thenReturn(response);

        // Act
        ResponseEntity<List<ContactInfoResponse>> actual = contactInfoController.getContactInfosForPerson(personId);

        // Assert
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertIterableEquals(response, actual.getBody());

        verify(contactInfoService).getContactInfosForPerson(personId);
        verifyNoMoreInteractions(contactInfoService);
    }

    @Test
    void addContactInfo_HappyPath() {
        // Arrange
        long personId = 13L;
        long contactInfoId = 17L;
        String location = "/persons/" + personId + "/contactInfos/" + contactInfoId;
        ContactInfoCreateRequest request = mock(ContactInfoCreateRequest.class);
        ContactInfoResponse response = mock(ContactInfoResponse.class);

        when(response.getId()).thenReturn(contactInfoId);
        when(contactInfoService.addContactInfo(any(), any())).thenReturn(response);

        // Act
        ResponseEntity<ContactInfoResponse> actual = contactInfoController.addContactInfo(personId, request);

        // Assert
        assertEquals(HttpStatus.CREATED, actual.getStatusCode());
        assertEquals(response, actual.getBody());

        assertNotNull(actual.getHeaders().getLocation());
        assertNotNull(actual.getHeaders().getLocation().toString());
        assertEquals(location, actual.getHeaders().getLocation().toString());

        verify(contactInfoService).addContactInfo(personId, request);
        verifyNoMoreInteractions(contactInfoService);
    }

    @Test
    void updateContactInfo_HappyPath() {
        // Arrange
        long personId = 13L;
        long contactInfoId = 17L;
        ContactInfoUpdateRequest request = mock(ContactInfoUpdateRequest.class);
        ContactInfoResponse response = mock(ContactInfoResponse.class);

        when(contactInfoService.updateContactInfo(any(), any(), any())).thenReturn(response);

        // Act
        ResponseEntity<ContactInfoResponse> actual = contactInfoController.updateContactInfo(personId, contactInfoId, request);

        // Assert
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(response, actual.getBody());

        verify(contactInfoService).updateContactInfo(personId, contactInfoId, request);
        verifyNoMoreInteractions(contactInfoService);
    }

    @Test
    void deleteContactInfo_HappyPath() {
        // Arrange
        long personId = 13L;
        long contactInfoId = 17L;

        // Act
        ResponseEntity<Void> actual = contactInfoController.deleteContactInfo(personId, contactInfoId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, actual.getStatusCode());

        verify(contactInfoService).deleteContactInfo(personId, contactInfoId);
        verifyNoMoreInteractions(contactInfoService);
    }
}