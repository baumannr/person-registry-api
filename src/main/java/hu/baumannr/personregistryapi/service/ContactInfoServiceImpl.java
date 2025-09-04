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
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

import static hu.baumannr.personregistryapi.constant.ErrorMessages.CONTACT_INFO_DOES_NOT_BELONG_TO_PERSON;
import static hu.baumannr.personregistryapi.constant.ErrorMessages.CONTACT_INFO_NOT_FOUND;
import static hu.baumannr.personregistryapi.constant.ErrorMessages.PERSON_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ContactInfoServiceImpl implements ContactInfoService {

    private final ContactInfoRepository contactInfoRepository;

    private final PersonRepository personRepository;
    
    private final ContactInfoMapper contactInfoMapper;

    @Override
    public List<ContactInfoResponse> getContactInfosForPerson(Long personId) {
        checkPersonExists(personId);
        List<ContactInfo> contactInfos = contactInfoRepository.findByPerson_Id(personId);

        return contactInfos.stream()
                .map(contactInfoMapper::convert)
                .toList();
    }

    @Override
    public ContactInfoResponse addContactInfo(Long personId, ContactInfoCreateRequest request) {
        Person person = getPerson(personId);
        ContactInfo contactInfo = contactInfoMapper.convert(request);
        person.addContactInfo(contactInfo);
        ContactInfo savedContactInfo = contactInfoRepository.save(contactInfo);
        return contactInfoMapper.convert(savedContactInfo);
    }

    @Override
    public ContactInfoResponse updateContactInfo(Long personId, Long contactInfoId, ContactInfoUpdateRequest request) {
        ContactInfo contactInfo = getContactInfo(contactInfoId);
        checkContactInfoBelongsToPerson(contactInfo, personId);

        contactInfoMapper.updateContactInfo(contactInfo, request);
        ContactInfo updatedContactInfo = contactInfoRepository.save(contactInfo);
        return contactInfoMapper.convert(updatedContactInfo);
    }

    @Override
    public void deleteContactInfo(Long personId, Long contactInfoId) {
        ContactInfo contactInfo = getContactInfo(contactInfoId);
        checkContactInfoBelongsToPerson(contactInfo, personId);

        contactInfoRepository.deleteById(contactInfoId);
    }

    private void checkPersonExists(Long personId) {
        if (! personRepository.existsById(personId)) {
            throw new PersonApiException(HttpStatus.NOT_FOUND, String.format(PERSON_NOT_FOUND, personId));
        }
    }

    private void checkContactInfoBelongsToPerson(ContactInfo contactInfo, Long personId) {
        if (! personId.equals(contactInfo.getPerson().getId())) {
            throw new PersonApiException(HttpStatus.NOT_FOUND,
                    String.format(CONTACT_INFO_DOES_NOT_BELONG_TO_PERSON, contactInfo.getId(), personId));
        }
    }

    private Person getPerson(Long personId) {
        return personRepository.findById(personId)
                .orElseThrow(() -> new PersonApiException(HttpStatus.NOT_FOUND,
                        String.format(PERSON_NOT_FOUND, personId)));
    }

    private ContactInfo getContactInfo(Long contactInfoId) {
        return contactInfoRepository.findById(contactInfoId)
                .orElseThrow(() -> new PersonApiException(HttpStatus.NOT_FOUND,
                        String.format(CONTACT_INFO_NOT_FOUND, contactInfoId)));
    }
}
