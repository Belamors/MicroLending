package einars.homework.microlending.service;

import einars.homework.microlending.domain.Loan;
import einars.homework.microlending.repository.LoanRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing Loan.
 */
@Service
@Transactional
public class LoanService {

    private final Logger log = LoggerFactory.getLogger(LoanService.class);

    private final LoanRepository loanRepository;

    public LoanService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    /**
     * Save a loan.
     *
     * @param loan the entity to save
     * @return the persisted entity
     */
    public Loan save(Loan loan) {
        log.debug("Request to save Loan : {}", loan);
        return loanRepository.save(loan);
    }

    /**
     *  Get all the loans.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Loan> findAll() {
        log.debug("Request to get all Loans");
        return loanRepository.findAll();
    }

    /**
     *  Get one loan by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Loan findOne(Long id) {
        log.debug("Request to get Loan : {}", id);
        return loanRepository.findOne(id);
    }

    /**
     *  Delete the  loan by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Loan : {}", id);
        loanRepository.delete(id);
    }
}
