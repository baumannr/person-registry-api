package hu.baumannr.personregistryapi.persistence.repository;

import hu.baumannr.personregistryapi.persistence.model.ContactInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for {@link ContactInfo} entity.
 */
@Repository
public interface ContactInfoRepository extends JpaRepository<ContactInfo, Long> {

    List<ContactInfo> findByPerson_Id(Long id);
}
