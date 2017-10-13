package einars.homework.microlending.web.rest;

import einars.homework.microlending.domain.LoanExtension;
import einars.homework.microlending.service.LoanExtensionService;
import einars.homework.microlending.web.rest.util.HeaderUtil;
import einars.homework.microlending.web.rest.util.ResponseUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing LoanExtension.
 */
@RestController
@RequestMapping("/api")
public class LoanExtensionResource {

    private final Logger log = LoggerFactory.getLogger(LoanExtensionResource.class);

    private static final String ENTITY_NAME = "loanExtension";

    private final LoanExtensionService loanExtensionService;

    public LoanExtensionResource(LoanExtensionService loanExtensionService) {
        this.loanExtensionService = loanExtensionService;
    }

    /**
     * POST  /loan-extensions : Create a new loanExtension.
     *
     * @param loanExtension the loanExtension to create
     * @return the ResponseEntity with status 201 (Created) and with body the new loanExtension, or with status 400 (Bad Request) if the loanExtension has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/loan-extensions")
    public ResponseEntity<LoanExtension> createLoanExtension(@Valid @RequestBody LoanExtension loanExtension) throws URISyntaxException {
        log.debug("REST request to save LoanExtension : {}", loanExtension);
        if (loanExtension.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new loanExtension cannot already have an ID")).body(null);
        }
        LoanExtension result = loanExtensionService.save(loanExtension);
        return ResponseEntity.created(new URI("/api/loan-extensions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /loan-extensions : Updates an existing loanExtension.
     *
     * @param loanExtension the loanExtension to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated loanExtension,
     * or with status 400 (Bad Request) if the loanExtension is not valid,
     * or with status 500 (Internal Server Error) if the loanExtension couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/loan-extensions")
    public ResponseEntity<LoanExtension> updateLoanExtension(@Valid @RequestBody LoanExtension loanExtension) throws URISyntaxException {
        log.debug("REST request to update LoanExtension : {}", loanExtension);
        if (loanExtension.getId() == null) {
            return createLoanExtension(loanExtension);
        }
        LoanExtension result = loanExtensionService.save(loanExtension);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, loanExtension.getId().toString()))
            .body(result);
    }

    /**
     * GET  /loan-extensions : get all the loanExtensions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of loanExtensions in body
     */
    @GetMapping("/loan-extensions")
    public List<LoanExtension> getAllLoanExtensions() {
        log.debug("REST request to get all LoanExtensions");
        return loanExtensionService.findAll();
        }

    /**
     * GET  /loan-extensions/:id : get the "id" loanExtension.
     *
     * @param id the id of the loanExtension to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the loanExtension, or with status 404 (Not Found)
     */
    @GetMapping("/loan-extensions/{id}")
    public ResponseEntity<LoanExtension> getLoanExtension(@PathVariable Long id) {
        log.debug("REST request to get LoanExtension : {}", id);
        LoanExtension loanExtension = loanExtensionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(loanExtension));
    }

    /**
     * DELETE  /loan-extensions/:id : delete the "id" loanExtension.
     *
     * @param id the id of the loanExtension to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/loan-extensions/{id}")
    public ResponseEntity<Void> deleteLoanExtension(@PathVariable Long id) {
        log.debug("REST request to delete LoanExtension : {}", id);
        loanExtensionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
