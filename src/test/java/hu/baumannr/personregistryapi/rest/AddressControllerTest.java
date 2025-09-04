package hu.baumannr.personregistryapi.rest;

import hu.baumannr.personregistryapi.rest.model.AddressCreateRequest;
import hu.baumannr.personregistryapi.rest.model.AddressResponse;
import hu.baumannr.personregistryapi.rest.model.AddressUpdateRequest;
import hu.baumannr.personregistryapi.service.AddressService;
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
class AddressControllerTest {

    @InjectMocks
    AddressController addressController;

    @Mock
    AddressService addressService;

    @Test
    void getAddressesForPerson_HappyPath() {
        // Arrange
        Long personId = 13L;
        AddressResponse response1 = mock(AddressResponse.class);
        AddressResponse response2 = mock(AddressResponse.class);
        List<AddressResponse> response = List.of(response1, response2);

        when(addressService.getAddressesForPerson(anyLong())).thenReturn(response);

        // Act
        ResponseEntity<List<AddressResponse>> actual = addressController.getAddressesForPerson(personId);

        // Assert
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertIterableEquals(response, actual.getBody());

        verify(addressService).getAddressesForPerson(personId);
        verifyNoMoreInteractions(addressService);
    }

    @Test
    void addAddress() {
        // Arrange
        long personId = 13L;
        long addressId = 17L;
        String location = "/persons/" + personId + "/addresses/" + addressId;
        AddressCreateRequest request = mock(AddressCreateRequest.class);
        AddressResponse response = mock(AddressResponse.class);

        when(response.getId()).thenReturn(addressId);
        when(addressService.addAddress(any(), any())).thenReturn(response);

        // Act
        ResponseEntity<AddressResponse> actual = addressController.addAddress(personId, request);

        // Assert
        assertEquals(HttpStatus.CREATED, actual.getStatusCode());
        assertEquals(response, actual.getBody());

        assertNotNull(actual.getHeaders().getLocation());
        assertNotNull(actual.getHeaders().getLocation().toString());
        assertEquals(location, actual.getHeaders().getLocation().toString());

        verify(addressService).addAddress(personId, request);
        verifyNoMoreInteractions(addressService);
    }

    @Test
    void updateAddress() {
        // Arrange
        long personId = 13L;
        long addressId = 17L;
        AddressUpdateRequest request = mock(AddressUpdateRequest.class);
        AddressResponse response = mock(AddressResponse.class);

        when(addressService.updateAddress(any(), any(), any())).thenReturn(response);

        // Act
        ResponseEntity<AddressResponse> actual = addressController.updateAddress(personId, addressId, request);

        // Assert
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(response, actual.getBody());

        verify(addressService).updateAddress(personId, addressId, request);
        verifyNoMoreInteractions(addressService);
    }

    @Test
    void deleteAddress() {
        // Arrange
        long personId = 13L;
        long addressId = 17L;

        // Act
        ResponseEntity<Void> actual = addressController.deleteAddress(personId, addressId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, actual.getStatusCode());

        verify(addressService).deleteAddress(personId, addressId);
        verifyNoMoreInteractions(addressService);
    }
}