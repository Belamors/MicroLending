package einars.homework.microlending.web.rest;

import einars.homework.microlending.domain.Loan;
import einars.homework.microlending.service.LoanService;
import einars.homework.microlending.web.rest.util.HeaderUtil;
import einars.homework.microlending.web.rest.util.ResponseUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Loan.
 */
@RestController
@RequestMapping("/api")
public class LoanResource {

    private final Logger log = LoggerFactory.getLogger(LoanResource.class);

    private static final String ENTITY_NAME = "loan";

    private final LoanService loanService;

    public LoanResource(LoanService loanService) {
        this.loanService = loanService;
    }

    /**
     * POST  /loans : Create a new loan.
     *
     * @param loan the loan to create
     * @return the ResponseEntity with status 201 (Created) and with body the new loan, or with status 400 (Bad Request) if the loan has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/loans")
    public ResponseEntity<Loan> createLoan(@Valid @RequestBody Loan loan) throws URISyntaxException {
        log.debug("REST request to save Loan : {}", loan);
        if (loan.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new loan cannot already have an ID")).body(null);
        }
        Loan result = loanService.save(loan);
        return ResponseEntity.created(new URI("/api/loans/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /loans : Updates an existing loan.
     *
     * @param loan the loan to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated loan,
     * or with status 400 (Bad Request) if the loan is not valid,
     * or with status 500 (Internal Server Error) if the loan couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/loans")
    public ResponseEntity<Loan> updateLoan(@Valid @RequestBody Loan loan) throws URISyntaxException {
        log.debug("REST request to update Loan : {}", loan);
        if (loan.getId() == null) {
            return createLoan(loan);
        }
        Loan result = loanService.save(loan);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, loan.getId().toString()))
            .body(result);
    }

    /**
     * GET  /loans : get all the loans.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of loans in body
     */
    @GetMapping("/loans")
    public List<Loan> getAllLoans() {
        log.debug("REST request to get all Loans");
        return loanService.findAll();
        }

    /**
     * GET  /loans/:id : get the "id" loan.
     *
     * @param id the id of the loan to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the loan, or with status 404 (Not Found)
     */
    @GetMapping("/loans/{id}")
    public ResponseEntity<Loan> getLoan(@PathVariable Long id) {
        log.debug("REST request to get Loan : {}", id);
        Loan loan = loanService.findOne(id);
        //return ResponseUtil.wrapOrNotFound(Optional.ofNullable(loan));
        return Optional.ofNullable(loan).map(response -> ResponseEntity.ok().body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /loans/:id : delete the "id" loan.
     *
     * @param id the id of the loan to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/loans/{id}")
    public ResponseEntity<Void> deleteLoan(@PathVariable Long id) {
        log.debug("REST request to delete Loan : {}", id);
        loanService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
