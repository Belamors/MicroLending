package einars.homework.microlending.service;

import einars.homework.microlending.domain.LoanExtension;
import einars.homework.microlending.repository.LoanExtensionRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing LoanExtension.
 */
@Service
@Transactional
public class LoanExtensionService {

    private final Logger log = LoggerFactory.getLogger(LoanExtensionService.class);

    private final LoanExtensionRepository loanExtensionRepository;

    public LoanExtensionService(LoanExtensionRepository loanExtensionRepository) {
        this.loanExtensionRepository = loanExtensionRepository;
    }

    /**
     * Save a loanExtension.
     *
     * @param loanExtension the entity to save
     * @return the persisted entity
     */
    public LoanExtension save(LoanExtension loanExtension) {
        log.debug("Request to save LoanExtension : {}", loanExtension);
        return loanExtensionRepository.save(loanExtension);
    }

    /**
     *  Get all the loanExtensions.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<LoanExtension> findAll() {
        log.debug("Request to get all LoanExtensions");
        return loanExtensionRepository.findAll();
    }

    /**
     *  Get one loanExtension by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public LoanExtension findOne(Long id) {
        log.debug("Request to get LoanExtension : {}", id);
        return loanExtensionRepository.findOne(id);
    }

    /**
     *  Delete the  loanExtension by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete LoanExtension : {}", id);
        loanExtensionRepository.delete(id);
    }
}
