package einars.homework.microlending.web.rest;

import einars.homework.microlending.MicrolendingApplication;

import einars.homework.microlending.domain.LoanExtension;
import einars.homework.microlending.domain.Loan;
import einars.homework.microlending.domain.Client;
import einars.homework.microlending.repository.LoanExtensionRepository;
import einars.homework.microlending.service.LoanExtensionService;

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
 * Test class for the LoanExtensionResource REST controller.
 *
 * @see LoanExtensionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MicrolendingApplication.class)
public class LoanExtensionResourceIntTest {

    private static final LocalDate DEFAULT_DURATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DURATION = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private LoanExtensionRepository loanExtensionRepository;

    @Autowired
    private LoanExtensionService loanExtensionService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private EntityManager em;

    private MockMvc restLoanExtensionMockMvc;

    private LoanExtension loanExtension;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LoanExtensionResource loanExtensionResource = new LoanExtensionResource(loanExtensionService);
        this.restLoanExtensionMockMvc = MockMvcBuilders.standaloneSetup(loanExtensionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LoanExtension createEntity(EntityManager em) {
        LoanExtension loanExtension = new LoanExtension()
            .duration(DEFAULT_DURATION);
        // Add required entity
        Loan loan = LoanResourceIntTest.createEntity(em);
        em.persist(loan);
        em.flush();
        loanExtension.setLoan(loan);
        // Add required entity
        Client client = ClientResourceIntTest.createEntity(em);
        em.persist(client);
        em.flush();
        loanExtension.setClient(client);
        return loanExtension;
    }

    @Before
    public void initTest() {
        loanExtension = createEntity(em);
    }

    @Test
    @Transactional
    public void createLoanExtension() throws Exception {
        int databaseSizeBeforeCreate = loanExtensionRepository.findAll().size();

        // Create the LoanExtension
        restLoanExtensionMockMvc.perform(post("/api/loan-extensions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loanExtension)))
            .andExpect(status().isCreated());

        // Validate the LoanExtension in the database
        List<LoanExtension> loanExtensionList = loanExtensionRepository.findAll();
        assertThat(loanExtensionList).hasSize(databaseSizeBeforeCreate + 1);
        LoanExtension testLoanExtension = loanExtensionList.get(loanExtensionList.size() - 1);
        assertThat(testLoanExtension.getDuration()).isEqualTo(DEFAULT_DURATION);
    }

    @Test
    @Transactional
    public void createLoanExtensionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = loanExtensionRepository.findAll().size();

        // Create the LoanExtension with an existing ID
        loanExtension.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLoanExtensionMockMvc.perform(post("/api/loan-extensions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loanExtension)))
            .andExpect(status().isBadRequest());

        // Validate the LoanExtension in the database
        List<LoanExtension> loanExtensionList = loanExtensionRepository.findAll();
        assertThat(loanExtensionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDurationIsRequired() throws Exception {
        int databaseSizeBeforeTest = loanExtensionRepository.findAll().size();
        // set the field null
        loanExtension.setDuration(null);

        // Create the LoanExtension, which fails.

        restLoanExtensionMockMvc.perform(post("/api/loan-extensions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loanExtension)))
            .andExpect(status().isBadRequest());

        List<LoanExtension> loanExtensionList = loanExtensionRepository.findAll();
        assertThat(loanExtensionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLoanExtensions() throws Exception {
        // Initialize the database
        loanExtensionRepository.saveAndFlush(loanExtension);

        // Get all the loanExtensionList
        restLoanExtensionMockMvc.perform(get("/api/loan-extensions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loanExtension.getId().intValue())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION.toString())));
    }

    @Test
    @Transactional
    public void getLoanExtension() throws Exception {
        // Initialize the database
        loanExtensionRepository.saveAndFlush(loanExtension);

        // Get the loanExtension
        restLoanExtensionMockMvc.perform(get("/api/loan-extensions/{id}", loanExtension.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(loanExtension.getId().intValue()))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLoanExtension() throws Exception {
        // Get the loanExtension
        restLoanExtensionMockMvc.perform(get("/api/loan-extensions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLoanExtension() throws Exception {
        // Initialize the database
        loanExtensionService.save(loanExtension);

        int databaseSizeBeforeUpdate = loanExtensionRepository.findAll().size();

        // Update the loanExtension
        LoanExtension updatedLoanExtension = loanExtensionRepository.findOne(loanExtension.getId());
        updatedLoanExtension
            .duration(UPDATED_DURATION);

        restLoanExtensionMockMvc.perform(put("/api/loan-extensions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLoanExtension)))
            .andExpect(status().isOk());

        // Validate the LoanExtension in the database
        List<LoanExtension> loanExtensionList = loanExtensionRepository.findAll();
        assertThat(loanExtensionList).hasSize(databaseSizeBeforeUpdate);
        LoanExtension testLoanExtension = loanExtensionList.get(loanExtensionList.size() - 1);
        assertThat(testLoanExtension.getDuration()).isEqualTo(UPDATED_DURATION);
    }

    @Test
    @Transactional
    public void updateNonExistingLoanExtension() throws Exception {
        int databaseSizeBeforeUpdate = loanExtensionRepository.findAll().size();

        // Create the LoanExtension

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLoanExtensionMockMvc.perform(put("/api/loan-extensions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loanExtension)))
            .andExpect(status().isCreated());

        // Validate the LoanExtension in the database
        List<LoanExtension> loanExtensionList = loanExtensionRepository.findAll();
        assertThat(loanExtensionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteLoanExtension() throws Exception {
        // Initialize the database
        loanExtensionService.save(loanExtension);

        int databaseSizeBeforeDelete = loanExtensionRepository.findAll().size();

        // Get the loanExtension
        restLoanExtensionMockMvc.perform(delete("/api/loan-extensions/{id}", loanExtension.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<LoanExtension> loanExtensionList = loanExtensionRepository.findAll();
        assertThat(loanExtensionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LoanExtension.class);
        LoanExtension loanExtension1 = new LoanExtension();
        loanExtension1.setId(1L);
        LoanExtension loanExtension2 = new LoanExtension();
        loanExtension2.setId(loanExtension1.getId());
        assertThat(loanExtension1).isEqualTo(loanExtension2);
        loanExtension2.setId(2L);
        assertThat(loanExtension1).isNotEqualTo(loanExtension2);
        loanExtension1.setId(null);
        assertThat(loanExtension1).isNotEqualTo(loanExtension2);
    }
}
