package hu.baumannr.personregistryapi.service;

import hu.baumannr.personregistryapi.exception.PersonApiException;
import hu.baumannr.personregistryapi.mapper.AddressMapper;
import hu.baumannr.personregistryapi.mapper.AddressTypeMapper;
import hu.baumannr.personregistryapi.persistence.model.Address;
import hu.baumannr.personregistryapi.persistence.model.AddressType;
import hu.baumannr.personregistryapi.persistence.model.Person;
import hu.baumannr.personregistryapi.persistence.repository.AddressRepository;
import hu.baumannr.personregistryapi.persistence.repository.PersonRepository;
import hu.baumannr.personregistryapi.rest.model.AddressCreateRequest;
import hu.baumannr.personregistryapi.rest.model.AddressResponse;
import hu.baumannr.personregistryapi.rest.model.AddressUpdateRequest;
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
class AddressServiceImplTest {

    @InjectMocks
    private AddressServiceImpl addressService;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private AddressMapper addressMapper;

    @Mock
    private AddressTypeMapper addressTypeMapper;

    @Test
    void getAddressesForPerson_HappyPath() {
        // Arrange
        long personId = 13L;
        Address address1 = mock(Address.class);
        Address address2 = mock(Address.class);
        List<Address> addresses = List.of(address1, address2);
        AddressResponse response1 = mock(AddressResponse.class);
        AddressResponse response2 = mock(AddressResponse.class);
        List<AddressResponse> expected = List.of(response1, response2);

        when(personRepository.existsById(any())).thenReturn(true);
        when(addressRepository.findByPerson_Id(any())).thenReturn(addresses);
        when(addressMapper.convert(address1)).thenReturn(response1);
        when(addressMapper.convert(address2)).thenReturn(response2);

        // Act
        List<AddressResponse> actual = addressService.getAddressesForPerson(personId);

        // Assert
        assertIterableEquals(expected, actual);

        InOrder inOrder = inOrder(personRepository, addressRepository, addressMapper);
        inOrder.verify(personRepository).existsById(personId);
        inOrder.verify(addressRepository).findByPerson_Id(personId);
        inOrder.verify(addressMapper).convert(address1);
        inOrder.verify(addressMapper).convert(address2);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void getAddressesForPerson_WhenPersonNotFound_ThenThrowsException() {
        // Arrange
        long personId = 13L;
        String message = "Person with id " + personId + " not found";
        LocalDateTime now = LocalDateTime.now();

        when(personRepository.existsById(any())).thenReturn(false);

        try (MockedStatic<LocalDateTime> mockedTime = Mockito.mockStatic(LocalDateTime.class)) {
            mockedTime.when(LocalDateTime::now).thenReturn(now);

            // Act
            PersonApiException exception = assertThrows(PersonApiException.class,
                    () -> addressService.getAddressesForPerson(personId));

            // Assert
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
            assertEquals(message, exception.getMessage());
        }
    }

    @Test
    void addAddress_HappyPath() {
        // Arrange
        Long personId = 13L;
        Person person = mock(Person.class);
        Address address = mock(Address.class);
        Address savedAddress = mock(Address.class);
        AddressCreateRequest request = mock(AddressCreateRequest.class);
        AddressResponse expected = mock(AddressResponse.class);

        when(personRepository.findById(any())).thenReturn(Optional.of(person));
        when(addressMapper.convert(any(AddressCreateRequest.class))).thenReturn(address);
        when(addressRepository.save(any())).thenReturn(savedAddress);
        when(addressMapper.convert(any(Address.class))).thenReturn(expected);

        // Act
        AddressResponse actual = addressService.addAddress(personId, request);

        // Assert
        assertEquals(expected, actual);

        InOrder inOrder = inOrder(personRepository, addressMapper, addressRepository);
        inOrder.verify(personRepository).findById(personId);
        inOrder.verify(addressMapper).convert(request);
        inOrder.verify(addressRepository).save(address);
        inOrder.verify(addressMapper).convert(savedAddress);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void addAddress_WhenPersonNotFound_ThenThrowsException() {
        // Arrange
        long personId = 13L;
        AddressCreateRequest request = mock(AddressCreateRequest.class);
        String message = "Person with id " + personId + " not found";
        LocalDateTime now = LocalDateTime.now();

        try (MockedStatic<LocalDateTime> mockedTime = Mockito.mockStatic(LocalDateTime.class)) {
            mockedTime.when(LocalDateTime::now).thenReturn(now);

            // Act
            PersonApiException exception = assertThrows(PersonApiException.class,
                    () -> addressService.addAddress(personId,request));

            // Assert
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
            assertEquals(message, exception.getMessage());
            assertEquals(now, exception.getTimestamp());
        }
    }

    @Test
    void addAddress_WhenAddressTypeAlreadyExists_ThenThrowsException() {
        // Arrange
        Long personId = 13L;
        Person person = mock(Person.class);
        Address address = mock(Address.class);
        AddressType addressType = AddressType.PERMANENT;
        AddressCreateRequest request = mock(AddressCreateRequest.class);
        AddressCreateRequest.TypeEnum type = AddressCreateRequest.TypeEnum.PERMANENT;
        LocalDateTime now = LocalDateTime.now();
        String message = "Address with type " + type + " already exists for person " + personId;

        when(personRepository.findById(any())).thenReturn(Optional.of(person));
        when(person.getId()).thenReturn(personId);
        when(person.getAddresses()).thenReturn(List.of(address));
        when(address.getType()).thenReturn(addressType);
        when(addressTypeMapper.convert(any())).thenReturn(addressType);
        when(request.getType()).thenReturn(type);

        try (MockedStatic<LocalDateTime> mockedTime = Mockito.mockStatic(LocalDateTime.class)) {
            mockedTime.when(LocalDateTime::now).thenReturn(now);

            // Act
            PersonApiException exception = assertThrows(PersonApiException.class,
                    () -> addressService.addAddress(personId, request));

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
            assertEquals(message, exception.getMessage());
            assertEquals(now, exception.getTimestamp());
        }
    }

    @Test
    void updateAddress_HappyPath() {
        // Arrange
        Long personId = 13L;
        Long addressId = 17L;
        Person person = mock(Person.class);
        Address address = mock(Address.class);
        Address updatedAddress = mock(Address.class);
        AddressUpdateRequest request = mock(AddressUpdateRequest.class);
        AddressResponse expected = mock(AddressResponse.class);

        when(address.getPerson()).thenReturn(person);
        when(person.getId()).thenReturn(personId);
        when(addressRepository.findById(any())).thenReturn(Optional.of(address));
        when(addressRepository.save(any())).thenReturn(updatedAddress);
        when(addressMapper.convert(any(Address.class))).thenReturn(expected);

        // Act
        AddressResponse actual = addressService.updateAddress(personId, addressId, request);

        // Assert
        assertEquals(expected, actual);

        InOrder inOrder = inOrder(personRepository, addressMapper, addressRepository);
        inOrder.verify(addressRepository).findById(addressId);
        inOrder.verify(addressMapper).updateAddress(address, request);
        inOrder.verify(addressRepository).save(address);
        inOrder.verify(addressMapper).convert(updatedAddress);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void updateAddress_WhenAddressNotFound_ThenThrowsException() {
        // Arrange
        long personId = 13L;
        long addressId = 17L;
        AddressUpdateRequest request = mock(AddressUpdateRequest.class);
        String message = "Address with id " + addressId + " not found";
        LocalDateTime now = LocalDateTime.now();

        try (MockedStatic<LocalDateTime> mockedTime = Mockito.mockStatic(LocalDateTime.class)) {
            mockedTime.when(LocalDateTime::now).thenReturn(now);

            // Act
            PersonApiException exception = assertThrows(PersonApiException.class,
                    () -> addressService.updateAddress(personId, addressId, request));

            // Assert
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
            assertEquals(message, exception.getMessage());
            assertEquals(now, exception.getTimestamp());
        }
    }

    @Test
    void updateAddress_WhenAddressDoesNotBelongToPerson_ThenThrowsException() {
        // Arrange
        Long otherPersonId = 51L;
        Long personId = 13L;
        Long addressId = 17L;
        Person person = mock(Person.class);
        Address address = mock(Address.class);
        AddressUpdateRequest request = mock(AddressUpdateRequest.class);
        LocalDateTime now = LocalDateTime.now();
        String message = "Address with id " + addressId + " does not belong to person " + otherPersonId;

        when(address.getId()).thenReturn(addressId);
        when(address.getPerson()).thenReturn(person);
        when(person.getId()).thenReturn(personId);
        when(addressRepository.findById(any())).thenReturn(Optional.of(address));

        try (MockedStatic<LocalDateTime> mockedTime = Mockito.mockStatic(LocalDateTime.class)) {
            mockedTime.when(LocalDateTime::now).thenReturn(now);

            PersonApiException exception = assertThrows(PersonApiException.class,
                    () -> addressService.updateAddress(otherPersonId, addressId, request));


            assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
            assertEquals(message, exception.getMessage());
            assertEquals(now, exception.getTimestamp());
        }
    }

    @Test
    void deleteAddress_HappyPath() {
        // Arrange
        Long personId = 13L;
        Long addressId = 17L;
        Person person = mock(Person.class);
        Address address = mock(Address.class);

        when(address.getPerson()).thenReturn(person);
        when(person.getId()).thenReturn(personId);
        when(addressRepository.findById(any())).thenReturn(Optional.of(address));

        // Act
        addressService.deleteAddress(personId, addressId);

        // Assert
        InOrder inOrder = inOrder(personRepository, addressMapper, addressRepository);
        inOrder.verify(addressRepository).findById(addressId);
        inOrder.verify(addressRepository).deleteById(addressId);
        inOrder.verifyNoMoreInteractions();
    }
}