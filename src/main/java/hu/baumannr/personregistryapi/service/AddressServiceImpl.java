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
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

import static hu.baumannr.personregistryapi.constant.ErrorMessages.ADDRESS_ALREADY_EXISTS;
import static hu.baumannr.personregistryapi.constant.ErrorMessages.ADDRESS_DOES_NOT_BELONG_TO_PERSON;
import static hu.baumannr.personregistryapi.constant.ErrorMessages.ADDRESS_NOT_FOUND;
import static hu.baumannr.personregistryapi.constant.ErrorMessages.PERSON_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    private final PersonRepository personRepository;

    private final AddressMapper addressMapper;

    private final AddressTypeMapper addressTypeMapper;

    @Override
    public List<AddressResponse> getAddressesForPerson(Long personId) {
        checkPersonExists(personId);
        List<Address> addresses = addressRepository.findByPerson_Id(personId);
        return addresses.stream()
                .map(addressMapper::convert)
                .toList();
    }

    @Override
    public AddressResponse addAddress(Long personId, AddressCreateRequest request) {
        Person person = getPerson(personId);
        checkAddressNotExistsByType(person, request);

        Address address = addressMapper.convert(request);
        person.addAddress(address);
        Address savedAddress = addressRepository.save(address);
        return addressMapper.convert(savedAddress);
    }

    @Override
    public AddressResponse updateAddress(Long personId, Long addressId, AddressUpdateRequest request) {
        Address address = getAddress(addressId);
        checkAddressBelongsToPerson(address, personId);

        addressMapper.updateAddress(address, request);
        Address updatedAddress = addressRepository.save(address);
        return addressMapper.convert(updatedAddress);
    }

    @Override
    public void deleteAddress(Long personId, Long addressId) {
        Address address = getAddress(addressId);
        checkAddressBelongsToPerson(address, personId);

        addressRepository.deleteById(addressId);
    }

    private void checkPersonExists(Long personId) {
        if (! personRepository.existsById(personId)) {
            throw new PersonApiException(HttpStatus.NOT_FOUND, String.format(PERSON_NOT_FOUND, personId));
        }
    }

    private void checkAddressBelongsToPerson(Address address, Long personId) {
        if (! personId.equals(address.getPerson().getId())) {
            throw new PersonApiException(HttpStatus.NOT_FOUND,
                    String.format(ADDRESS_DOES_NOT_BELONG_TO_PERSON, address.getId(), personId));
        }
    }

    private void checkAddressNotExistsByType(Person person, AddressCreateRequest request) {
        AddressType addressType = addressTypeMapper.convert(request.getType());
        boolean exists = person.getAddresses().stream()
                .anyMatch(a -> a.getType().equals(addressType));

        if (exists) {
            throw new PersonApiException(HttpStatus.BAD_REQUEST,
                    String.format(ADDRESS_ALREADY_EXISTS, request.getType().getValue(), person.getId()));
        }
    }

    private Person getPerson(Long personId) {
        return personRepository.findById(personId)
                .orElseThrow(() -> new PersonApiException(HttpStatus.NOT_FOUND,
                        String.format(PERSON_NOT_FOUND, personId)));
    }

    private Address getAddress(Long addressId) {
        return addressRepository.findById(addressId)
                .orElseThrow(() -> new PersonApiException(HttpStatus.NOT_FOUND,
                        String.format(ADDRESS_NOT_FOUND, addressId)));
    }
}
