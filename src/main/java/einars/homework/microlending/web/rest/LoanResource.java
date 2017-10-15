package einars.homework.microlending.web.rest;

import einars.homework.microlending.domain.Loan;
import einars.homework.microlending.service.LoanService;
import einars.homework.microlending.service.RiskAnalizesService;

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
public class LoanResource {

	private final Logger log = LoggerFactory.getLogger(LoanResource.class);

	private final LoanService loanService;

	private final RiskAnalizesService riskAnalize;

	public LoanResource(LoanService loanService, RiskAnalizesService riskAnalize) {
		this.loanService = loanService;
		this.riskAnalize = riskAnalize;
	}

	/**
	 * POST /loans : Create a new loan.
	 *
	 * @param loan
	 *            the loan to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         loan, or with status 400 (Bad Request) if the loan has already an ID,
	 *         or with status 403 (Forbidden) if the risk analizes woun't pass
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/loan")
	public ResponseEntity<Loan> createLoan(@Valid @RequestBody Loan loan) throws URISyntaxException {
		log.debug("REST request to save Loan : {}", loan);
		if (loan.getId() != null) {
			return ResponseEntity.badRequest().body(null);
		}
		if (!riskAnalize.giveLoan(loan)) {
			return new ResponseEntity<Loan>(HttpStatus.FORBIDDEN);
		}
		Loan result = loanService.save(loan);
		return ResponseEntity.created(new URI("/loans/" + result.getId())).body(result);
	}

	/**
	 * PUT /loans : Updates an existing loan.
	 *
	 * @param loan
	 *            the loan to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         loan, or with status 400 (Bad Request) if the loan is not valid, or
	 *         with status 500 (Internal Server Error) if the loan couldn't be
	 *         updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/update-loan")
	public ResponseEntity<Loan> updateLoan(@Valid @RequestBody Loan loan) throws URISyntaxException {
		log.debug("REST request to update Loan : {}", loan);
		if (loan.getId() == null) {
			return createLoan(loan);
		}
		Loan result = loanService.save(loan);
		return ResponseEntity.ok().body(result);
	}

	/**
	 * GET /loans : get all the loans.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of loans in body
	 */
	@GetMapping("/loans")
	public List<Loan> getAllLoans() {
		log.debug("REST request to get all Loans");
		return loanService.findAll();
	}

	/**
	 * GET /loans/:id : get the "id" loan.
	 *
	 * @param id
	 *            the id of the loan to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the loan, or
	 *         with status 404 (Not Found)
	 */
	@GetMapping("/loans/{id}")
	public ResponseEntity<Loan> getLoan(@PathVariable Long id) {
		log.debug("REST request to get Loan : {}", id);
		Loan loan = loanService.findOne(id);
		return Optional.ofNullable(loan).map(response -> ResponseEntity.ok().body(response))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * DELETE /loans/:id : delete the "id" loan.
	 *
	 * @param id
	 *            the id of the loan to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/loans/{id}")
	public ResponseEntity<Void> deleteLoan(@PathVariable Long id) {
		log.debug("REST request to delete Loan : {}", id);
		loanService.delete(id);
		return ResponseEntity.ok().build();
	}
}
