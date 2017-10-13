package einars.homework.microlending.repository;

import org.springframework.stereotype.Repository;

import einars.homework.microlending.domain.Client;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Client entity.
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

}
