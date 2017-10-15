package einars.homework.microlending.web.rest;

import einars.homework.microlending.MicrolendingApplication;

import einars.homework.microlending.domain.Loan;
import einars.homework.microlending.domain.Client;
import einars.homework.microlending.repository.LoanRepository;
import einars.homework.microlending.service.LoanService;
import einars.homework.microlending.service.RiskAnalizesService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the LoanResource REST controller.
 *
 * @see LoanResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MicrolendingApplication.class)
public class LoanResourceIntTest {

    private static final Float DEFAULT_AMOUNT = 0F;
    private static final Float UPDATED_AMOUNT = 1F;

    private static final Boolean DEFAULT_ACCEPTED = false;
    private static final Boolean UPDATED_ACCEPTED = true;

    private static final LocalDate DEFAULT_TERM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TERM = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_IP = "1\\d\\d\\<254\\*252\\o23\\d";
    private static final String UPDATED_IP = "254\\4251\\s21\\d\\t255";

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private LoanService loanService;
    @Autowired
    private RiskAnalizesService riskAnalize;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private EntityManager em;

    private MockMvc restLoanMockMvc;

    private Loan loan;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LoanResource loanResource = new LoanResource(loanService, riskAnalize);
        this.restLoanMockMvc = MockMvcBuilders.standaloneSetup(loanResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Loan createEntity(EntityManager em) {
        Loan loan = new Loan()
            .amount(DEFAULT_AMOUNT)
            .accepted(DEFAULT_ACCEPTED)
            .term(DEFAULT_TERM)
            .ip(DEFAULT_IP);
        // Add required entity
        Client client = ClientResourceIntTest.createEntity(em);
        em.persist(client);
        em.flush();
        loan.setClient(client);
        return loan;
    }

    @Before
    public void initTest() {
        loan = createEntity(em);
    }

    @Test
    @Transactional
    public void createLoan() throws Exception {
        int databaseSizeBeforeCreate = loanRepository.findAll().size();

        // Create the Loan
        restLoanMockMvc.perform(post("/loan")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loan)))
            .andExpect(status().isCreated());

        // Validate the Loan in the database
        List<Loan> loanList = loanRepository.findAll();
        assertThat(loanList).hasSize(databaseSizeBeforeCreate + 1);
        Loan testLoan = loanList.get(loanList.size() - 1);
        assertThat(testLoan.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testLoan.isAccepted()).isEqualTo(DEFAULT_ACCEPTED);
        assertThat(testLoan.getTerm()).isEqualTo(DEFAULT_TERM);
        assertThat(testLoan.getIp()).isEqualTo(DEFAULT_IP);
    }

    @Test
    @Transactional
    public void createLoanWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = loanRepository.findAll().size();

        // Create the Loan with an existing ID
        loan.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLoanMockMvc.perform(post("/loan")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loan)))
            .andExpect(status().isBadRequest());

        // Validate the Loan in the database
        List<Loan> loanList = loanRepository.findAll();
        assertThat(loanList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = loanRepository.findAll().size();
        // set the field null
        loan.setAmount(null);

        // Create the Loan, which fails.

        restLoanMockMvc.perform(post("/loan")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loan)))
            .andExpect(status().isBadRequest());

        List<Loan> loanList = loanRepository.findAll();
        assertThat(loanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAcceptedIsRequired() throws Exception {
        int databaseSizeBeforeTest = loanRepository.findAll().size();
        // set the field null
        loan.setAccepted(null);

        // Create the Loan, which fails.

        restLoanMockMvc.perform(post("/loan")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loan)))
            .andExpect(status().isBadRequest());

        List<Loan> loanList = loanRepository.findAll();
        assertThat(loanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIpIsRequired() throws Exception {
        int databaseSizeBeforeTest = loanRepository.findAll().size();
        // set the field null
        loan.setIp(null);

        // Create the Loan, which fails.

        restLoanMockMvc.perform(post("/loan")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loan)))
            .andExpect(status().isBadRequest());

        List<Loan> loanList = loanRepository.findAll();
        assertThat(loanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLoans() throws Exception {
        // Initialize the database
        loanRepository.saveAndFlush(loan);

        // Get all the loanList
        restLoanMockMvc.perform(get("/loans?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loan.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].accepted").value(hasItem(DEFAULT_ACCEPTED.booleanValue())))
            .andExpect(jsonPath("$.[*].term").value(hasItem(DEFAULT_TERM.toString())))
            .andExpect(jsonPath("$.[*].ip").value(hasItem(DEFAULT_IP.toString())));
    }

    @Test
    @Transactional
    public void getLoan() throws Exception {
        // Initialize the database
        loanRepository.saveAndFlush(loan);

        // Get the loan
        restLoanMockMvc.perform(get("/loans/{id}", loan.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(loan.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.accepted").value(DEFAULT_ACCEPTED.booleanValue()))
            .andExpect(jsonPath("$.term").value(DEFAULT_TERM.toString()))
            .andExpect(jsonPath("$.ip").value(DEFAULT_IP.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLoan() throws Exception {
        // Get the loan
        restLoanMockMvc.perform(get("/loans/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLoan() throws Exception {
        // Initialize the database
        loanService.save(loan);

        int databaseSizeBeforeUpdate = loanRepository.findAll().size();

        // Update the loan
        Loan updatedLoan = loanRepository.findOne(loan.getId());
        updatedLoan
            .amount(UPDATED_AMOUNT)
            .accepted(UPDATED_ACCEPTED)
            .term(UPDATED_TERM)
            .ip(UPDATED_IP);

        restLoanMockMvc.perform(put("/update-loan")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLoan)))
            .andExpect(status().isOk());

        // Validate the Loan in the database
        List<Loan> loanList = loanRepository.findAll();
        assertThat(loanList).hasSize(databaseSizeBeforeUpdate);
        Loan testLoan = loanList.get(loanList.size() - 1);
        assertThat(testLoan.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testLoan.isAccepted()).isEqualTo(UPDATED_ACCEPTED);
        assertThat(testLoan.getTerm()).isEqualTo(UPDATED_TERM);
        assertThat(testLoan.getIp()).isEqualTo(UPDATED_IP);
    }

    @Test
    @Transactional
    public void updateNonExistingLoan() throws Exception {
        int databaseSizeBeforeUpdate = loanRepository.findAll().size();

        // Create the Loan

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLoanMockMvc.perform(put("/update-loan")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loan)))
            .andExpect(status().isCreated());

        // Validate the Loan in the database
        List<Loan> loanList = loanRepository.findAll();
        assertThat(loanList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteLoan() throws Exception {
        // Initialize the database
        loanService.save(loan);

        int databaseSizeBeforeDelete = loanRepository.findAll().size();

        // Get the loan
        restLoanMockMvc.perform(delete("/loans/{id}", loan.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Loan> loanList = loanRepository.findAll();
        assertThat(loanList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Loan.class);
        Loan loan1 = new Loan();
        loan1.setId(1L);
        Loan loan2 = new Loan();
        loan2.setId(loan1.getId());
        assertThat(loan1).isEqualTo(loan2);
        loan2.setId(2L);
        assertThat(loan1).isNotEqualTo(loan2);
        loan1.setId(null);
        assertThat(loan1).isNotEqualTo(loan2);
    }
}
