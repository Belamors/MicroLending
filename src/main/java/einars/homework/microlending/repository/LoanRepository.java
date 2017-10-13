package einars.homework.microlending.repository;

import org.springframework.stereotype.Repository;

import einars.homework.microlending.domain.Loan;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Loan entity.
 */
@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

}
