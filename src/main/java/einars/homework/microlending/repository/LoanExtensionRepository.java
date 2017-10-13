package einars.homework.microlending.repository;

import org.springframework.stereotype.Repository;

import einars.homework.microlending.domain.LoanExtension;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the LoanExtension entity.
 */
@Repository
public interface LoanExtensionRepository extends JpaRepository<LoanExtension, Long> {

}
