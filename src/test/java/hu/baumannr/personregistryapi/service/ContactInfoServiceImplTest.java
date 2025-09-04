package hu.baumannr.personregistryapi.service;

import hu.baumannr.personregistryapi.exception.PersonApiException;
import hu.baumannr.personregistryapi.mapper.ContactInfoMapper;
import hu.baumannr.personregistryapi.persistence.model.ContactInfo;
import hu.baumannr.personregistryapi.persistence.model.Person;
import hu.baumannr.personregistryapi.persistence.repository.ContactInfoRepository;
import hu.baumannr.personregistryapi.persistence.repository.PersonRepository;
import hu.baumannr.personregistryapi.rest.model.ContactInfoCreateRequest;
import hu.baumannr.personregistryapi.rest.model.ContactInfoResponse;
import hu.baumannr.personregistryapi.rest.model.ContactInfoUpdateRequest;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContactInfoServiceImplTest {
    
    @InjectMocks
    private ContactInfoServiceImpl contactInfoService;
    
    @Mock
    private ContactInfoRepository contactInfoRepository;
    
    @Mock
    private PersonRepository personRepository;
    
    @Mock
    private ContactInfoMapper contactInfoMapper;

    @Test
    void getContactInfosForPerson_HappyPath() {
        // Arrange
        long personId = 13L;
        ContactInfo contactInfo1 = mock(ContactInfo.class);
        ContactInfo contactInfo2 = mock(ContactInfo.class);
        List<ContactInfo> contactInfoes = List.of(contactInfo1, contactInfo2);
        ContactInfoResponse response1 = mock(ContactInfoResponse.class);
        ContactInfoResponse response2 = mock(ContactInfoResponse.class);
        List<ContactInfoResponse> expected = List.of(response1, response2);

        when(personRepository.existsById(any())).thenReturn(true);
        when(contactInfoRepository.findByPerson_Id(any())).thenReturn(contactInfoes);
        when(contactInfoMapper.convert(contactInfo1)).thenReturn(response1);
        when(contactInfoMapper.convert(contactInfo2)).thenReturn(response2);

        // Act
        List<ContactInfoResponse> actual = contactInfoService.getContactInfosForPerson(personId);

        // Assert
        assertIterableEquals(expected, actual);

        InOrder inOrder = inOrder(personRepository, contactInfoRepository, contactInfoMapper);
        inOrder.verify(personRepository).existsById(personId);
        inOrder.verify(contactInfoRepository).findByPerson_Id(personId);
        inOrder.verify(contactInfoMapper).convert(contactInfo1);
        inOrder.verify(contactInfoMapper).convert(contactInfo2);
        inOrder.verifyNoMoreInteractions();
    }
    
    @Test
    void getContactInfosForPerson_WhenPersonNotFound_ThenThrowsException() {
        // Arrange
        long personId = 13L;
        String message = "Person with id " + personId + " not found";
        LocalDateTime now = LocalDateTime.now();

        when(personRepository.existsById(any())).thenReturn(false);

        try (MockedStatic<LocalDateTime> mockedTime = Mockito.mockStatic(LocalDateTime.class)) {
            mockedTime.when(LocalDateTime::now).thenReturn(now);

            // Act
            PersonApiException exception = assertThrows(PersonApiException.class,
                    () -> contactInfoService.getContactInfosForPerson(personId));

            // Assert
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
            assertEquals(message, exception.getMessage());
            assertEquals(now, exception.getTimestamp());
        }
    }

    @Test
    void addContactInfo_HappyPath() {
        // Arrange
        long personId = 13L;
        Person person = mock(Person.class);
        ContactInfo contactInfo = mock(ContactInfo.class);
        ContactInfo savedContactInfo = mock(ContactInfo.class);
        ContactInfoCreateRequest request = mock(ContactInfoCreateRequest.class);
        ContactInfoResponse expected = mock(ContactInfoResponse.class);

        when(personRepository.findById(any())).thenReturn(Optional.of(person));
        when(contactInfoMapper.convert(any(ContactInfoCreateRequest.class))).thenReturn(contactInfo);
        when(contactInfoRepository.save(any())).thenReturn(savedContactInfo);
        when(contactInfoMapper.convert(any(ContactInfo.class))).thenReturn(expected);

        // Act
        ContactInfoResponse actual = contactInfoService.addContactInfo(personId, request);

        // Assert
        assertEquals(expected, actual);

        InOrder inOrder = inOrder(personRepository, contactInfoMapper, contactInfoRepository);
        inOrder.verify(personRepository).findById(personId);
        inOrder.verify(contactInfoMapper).convert(request);
        inOrder.verify(contactInfoRepository).save(contactInfo);
        inOrder.verify(contactInfoMapper).convert(savedContactInfo);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void addContactInfo_WhenPersonNotFound_ThenThrowsException() {
        // Arrange
        long personId = 13L;
        ContactInfoCreateRequest request = mock(ContactInfoCreateRequest.class);
        String message = "Person with id " + personId + " not found";
        LocalDateTime now = LocalDateTime.now();

        try (MockedStatic<LocalDateTime> mockedTime = Mockito.mockStatic(LocalDateTime.class)) {
            mockedTime.when(LocalDateTime::now).thenReturn(now);

            // Act
            PersonApiException exception = assertThrows(PersonApiException.class,
                    () -> contactInfoService.addContactInfo(personId,request));

            // Assert
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
            assertEquals(message, exception.getMessage());
            assertEquals(now, exception.getTimestamp());
        }
    }

    @Test
    void updateContactInfo_HappyPath() {
        // Arrange
        long personId = 13L;
        long contactInfoId = 17L;
        Person person = mock(Person.class);
        ContactInfo contactInfo = mock(ContactInfo.class);
        ContactInfo updatedContactInfo = mock(ContactInfo.class);
        ContactInfoUpdateRequest request = mock(ContactInfoUpdateRequest.class);
        ContactInfoResponse expected = mock(ContactInfoResponse.class);

        when(contactInfo.getPerson()).thenReturn(person);
        when(person.getId()).thenReturn(personId);
        when(contactInfoRepository.findById(any())).thenReturn(Optional.of(contactInfo));
        when(contactInfoRepository.save(any())).thenReturn(updatedContactInfo);
        when(contactInfoMapper.convert(any(ContactInfo.class))).thenReturn(expected);

        // Act
        ContactInfoResponse actual = contactInfoService.updateContactInfo(personId, contactInfoId, request);

        // Assert
        assertEquals(expected, actual);

        InOrder inOrder = inOrder(personRepository, contactInfoMapper, contactInfoRepository);
        inOrder.verify(contactInfoRepository).findById(contactInfoId);
        inOrder.verify(contactInfoMapper).updateContactInfo(contactInfo, request);
        inOrder.verify(contactInfoRepository).save(contactInfo);
        inOrder.verify(contactInfoMapper).convert(updatedContactInfo);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void updateContactInfo_WhenContactInfoNotFound_ThenThrowsException() {
        // Arrange
        long personId = 13L;
        long contactInfoId = 17L;
        ContactInfoUpdateRequest request = mock(ContactInfoUpdateRequest.class);
        String message = "Contact information with id " + contactInfoId + " not found";
        LocalDateTime now = LocalDateTime.now();

        try (MockedStatic<LocalDateTime> mockedTime = Mockito.mockStatic(LocalDateTime.class)) {
            mockedTime.when(LocalDateTime::now).thenReturn(now);

            // Act
            PersonApiException exception = assertThrows(PersonApiException.class,
                    () -> contactInfoService.updateContactInfo(personId, contactInfoId, request));

            // Assert
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
            assertEquals(message, exception.getMessage());
            assertEquals(now, exception.getTimestamp());
        }
    }

    @Test
    void updateContactInfo_WhenContactInfoDoesNotBelongToPerson_ThenThrowsException() {
        // Arrange
        Long otherPersonId = 51L;
        Long personId = 13L;
        Long contactInfoId = 17L;
        Person person = mock(Person.class);
        ContactInfo contactInfo = mock(ContactInfo.class);
        ContactInfoUpdateRequest request = mock(ContactInfoUpdateRequest.class);
        LocalDateTime now = LocalDateTime.now();
        String message = "Contact information with id " + contactInfoId + " does not belong to person " + otherPersonId;

        when(contactInfo.getId()).thenReturn(contactInfoId);
        when(contactInfo.getPerson()).thenReturn(person);
        when(person.getId()).thenReturn(personId);
        when(contactInfoRepository.findById(any())).thenReturn(Optional.of(contactInfo));

        try (MockedStatic<LocalDateTime> mockedTime = Mockito.mockStatic(LocalDateTime.class)) {
            mockedTime.when(LocalDateTime::now).thenReturn(now);

            PersonApiException exception = assertThrows(PersonApiException.class,
                    () -> contactInfoService.updateContactInfo(otherPersonId, contactInfoId, request));

            assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
            assertEquals(message, exception.getMessage());
            assertEquals(now, exception.getTimestamp());
        }
    }

    @Test
    void deleteContactInfo() {
        // Arrange
        Long personId = 13L;
        Long contactInfoId = 17L;
        Person person = mock(Person.class);
        ContactInfo contactInfo = mock(ContactInfo.class);

        when(contactInfo.getPerson()).thenReturn(person);
        when(person.getId()).thenReturn(personId);
        when(contactInfoRepository.findById(any())).thenReturn(Optional.of(contactInfo));

        // Act
        contactInfoService.deleteContactInfo(personId, contactInfoId);

        // Assert
        InOrder inOrder = inOrder(personRepository, contactInfoMapper, contactInfoRepository);
        inOrder.verify(contactInfoRepository).findById(contactInfoId);
        inOrder.verify(contactInfoRepository).deleteById(contactInfoId);
        inOrder.verifyNoMoreInteractions();
    }
}