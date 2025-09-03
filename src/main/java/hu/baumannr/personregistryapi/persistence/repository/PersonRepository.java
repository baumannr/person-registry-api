package hu.baumannr.personregistryapi.persistence.repository;

import hu.baumannr.personregistryapi.persistence.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link Person} entity.
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
}
