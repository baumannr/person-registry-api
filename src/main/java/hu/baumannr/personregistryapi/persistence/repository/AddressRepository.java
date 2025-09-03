package hu.baumannr.personregistryapi.persistence.repository;

import hu.baumannr.personregistryapi.persistence.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link Address}
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}
