package y.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static y.web.rest.TestUtil.sameNumber;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import y.IntegrationTest;
import y.domain.Sale;
import y.repository.EntityManager;
import y.repository.SaleRepository;
import y.service.dto.SaleDTO;
import y.service.mapper.SaleMapper;

/**
 * Integration tests for the {@link SaleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class SaleResourceIT {

    private static final LocalDate DEFAULT_SALE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SALE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final BigDecimal DEFAULT_SALE_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_SALE_PRICE = new BigDecimal(2);

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final String ENTITY_API_URL = "/api/sales";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private SaleMapper saleMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Sale sale;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sale createEntity(EntityManager em) {
        Sale sale = new Sale().saleDate(DEFAULT_SALE_DATE).salePrice(DEFAULT_SALE_PRICE).quantity(DEFAULT_QUANTITY);
        return sale;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sale createUpdatedEntity(EntityManager em) {
        Sale sale = new Sale().saleDate(UPDATED_SALE_DATE).salePrice(UPDATED_SALE_PRICE).quantity(UPDATED_QUANTITY);
        return sale;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Sale.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        sale = createEntity(em);
    }

    @Test
    void createSale() throws Exception {
        int databaseSizeBeforeCreate = saleRepository.findAll().collectList().block().size();
        // Create the Sale
        SaleDTO saleDTO = saleMapper.toDto(sale);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(saleDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Sale in the database
        List<Sale> saleList = saleRepository.findAll().collectList().block();
        assertThat(saleList).hasSize(databaseSizeBeforeCreate + 1);
        Sale testSale = saleList.get(saleList.size() - 1);
        assertThat(testSale.getSaleDate()).isEqualTo(DEFAULT_SALE_DATE);
        assertThat(testSale.getSalePrice()).isEqualByComparingTo(DEFAULT_SALE_PRICE);
        assertThat(testSale.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
    }

    @Test
    void createSaleWithExistingId() throws Exception {
        // Create the Sale with an existing ID
        sale.setId(1L);
        SaleDTO saleDTO = saleMapper.toDto(sale);

        int databaseSizeBeforeCreate = saleRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(saleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Sale in the database
        List<Sale> saleList = saleRepository.findAll().collectList().block();
        assertThat(saleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkSaleDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = saleRepository.findAll().collectList().block().size();
        // set the field null
        sale.setSaleDate(null);

        // Create the Sale, which fails.
        SaleDTO saleDTO = saleMapper.toDto(sale);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(saleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Sale> saleList = saleRepository.findAll().collectList().block();
        assertThat(saleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkSalePriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = saleRepository.findAll().collectList().block().size();
        // set the field null
        sale.setSalePrice(null);

        // Create the Sale, which fails.
        SaleDTO saleDTO = saleMapper.toDto(sale);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(saleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Sale> saleList = saleRepository.findAll().collectList().block();
        assertThat(saleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = saleRepository.findAll().collectList().block().size();
        // set the field null
        sale.setQuantity(null);

        // Create the Sale, which fails.
        SaleDTO saleDTO = saleMapper.toDto(sale);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(saleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Sale> saleList = saleRepository.findAll().collectList().block();
        assertThat(saleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllSales() {
        // Initialize the database
        saleRepository.save(sale).block();

        // Get all the saleList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(sale.getId().intValue()))
            .jsonPath("$.[*].saleDate")
            .value(hasItem(DEFAULT_SALE_DATE.toString()))
            .jsonPath("$.[*].salePrice")
            .value(hasItem(sameNumber(DEFAULT_SALE_PRICE)))
            .jsonPath("$.[*].quantity")
            .value(hasItem(DEFAULT_QUANTITY));
    }

    @Test
    void getSale() {
        // Initialize the database
        saleRepository.save(sale).block();

        // Get the sale
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, sale.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(sale.getId().intValue()))
            .jsonPath("$.saleDate")
            .value(is(DEFAULT_SALE_DATE.toString()))
            .jsonPath("$.salePrice")
            .value(is(sameNumber(DEFAULT_SALE_PRICE)))
            .jsonPath("$.quantity")
            .value(is(DEFAULT_QUANTITY));
    }

    @Test
    void getNonExistingSale() {
        // Get the sale
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingSale() throws Exception {
        // Initialize the database
        saleRepository.save(sale).block();

        int databaseSizeBeforeUpdate = saleRepository.findAll().collectList().block().size();

        // Update the sale
        Sale updatedSale = saleRepository.findById(sale.getId()).block();
        updatedSale.saleDate(UPDATED_SALE_DATE).salePrice(UPDATED_SALE_PRICE).quantity(UPDATED_QUANTITY);
        SaleDTO saleDTO = saleMapper.toDto(updatedSale);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, saleDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(saleDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Sale in the database
        List<Sale> saleList = saleRepository.findAll().collectList().block();
        assertThat(saleList).hasSize(databaseSizeBeforeUpdate);
        Sale testSale = saleList.get(saleList.size() - 1);
        assertThat(testSale.getSaleDate()).isEqualTo(UPDATED_SALE_DATE);
        assertThat(testSale.getSalePrice()).isEqualByComparingTo(UPDATED_SALE_PRICE);
        assertThat(testSale.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    void putNonExistingSale() throws Exception {
        int databaseSizeBeforeUpdate = saleRepository.findAll().collectList().block().size();
        sale.setId(longCount.incrementAndGet());

        // Create the Sale
        SaleDTO saleDTO = saleMapper.toDto(sale);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, saleDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(saleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Sale in the database
        List<Sale> saleList = saleRepository.findAll().collectList().block();
        assertThat(saleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchSale() throws Exception {
        int databaseSizeBeforeUpdate = saleRepository.findAll().collectList().block().size();
        sale.setId(longCount.incrementAndGet());

        // Create the Sale
        SaleDTO saleDTO = saleMapper.toDto(sale);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(saleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Sale in the database
        List<Sale> saleList = saleRepository.findAll().collectList().block();
        assertThat(saleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamSale() throws Exception {
        int databaseSizeBeforeUpdate = saleRepository.findAll().collectList().block().size();
        sale.setId(longCount.incrementAndGet());

        // Create the Sale
        SaleDTO saleDTO = saleMapper.toDto(sale);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(saleDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Sale in the database
        List<Sale> saleList = saleRepository.findAll().collectList().block();
        assertThat(saleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateSaleWithPatch() throws Exception {
        // Initialize the database
        saleRepository.save(sale).block();

        int databaseSizeBeforeUpdate = saleRepository.findAll().collectList().block().size();

        // Update the sale using partial update
        Sale partialUpdatedSale = new Sale();
        partialUpdatedSale.setId(sale.getId());

        partialUpdatedSale.saleDate(UPDATED_SALE_DATE).salePrice(UPDATED_SALE_PRICE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSale.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSale))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Sale in the database
        List<Sale> saleList = saleRepository.findAll().collectList().block();
        assertThat(saleList).hasSize(databaseSizeBeforeUpdate);
        Sale testSale = saleList.get(saleList.size() - 1);
        assertThat(testSale.getSaleDate()).isEqualTo(UPDATED_SALE_DATE);
        assertThat(testSale.getSalePrice()).isEqualByComparingTo(UPDATED_SALE_PRICE);
        assertThat(testSale.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
    }

    @Test
    void fullUpdateSaleWithPatch() throws Exception {
        // Initialize the database
        saleRepository.save(sale).block();

        int databaseSizeBeforeUpdate = saleRepository.findAll().collectList().block().size();

        // Update the sale using partial update
        Sale partialUpdatedSale = new Sale();
        partialUpdatedSale.setId(sale.getId());

        partialUpdatedSale.saleDate(UPDATED_SALE_DATE).salePrice(UPDATED_SALE_PRICE).quantity(UPDATED_QUANTITY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSale.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSale))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Sale in the database
        List<Sale> saleList = saleRepository.findAll().collectList().block();
        assertThat(saleList).hasSize(databaseSizeBeforeUpdate);
        Sale testSale = saleList.get(saleList.size() - 1);
        assertThat(testSale.getSaleDate()).isEqualTo(UPDATED_SALE_DATE);
        assertThat(testSale.getSalePrice()).isEqualByComparingTo(UPDATED_SALE_PRICE);
        assertThat(testSale.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    void patchNonExistingSale() throws Exception {
        int databaseSizeBeforeUpdate = saleRepository.findAll().collectList().block().size();
        sale.setId(longCount.incrementAndGet());

        // Create the Sale
        SaleDTO saleDTO = saleMapper.toDto(sale);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, saleDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(saleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Sale in the database
        List<Sale> saleList = saleRepository.findAll().collectList().block();
        assertThat(saleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchSale() throws Exception {
        int databaseSizeBeforeUpdate = saleRepository.findAll().collectList().block().size();
        sale.setId(longCount.incrementAndGet());

        // Create the Sale
        SaleDTO saleDTO = saleMapper.toDto(sale);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(saleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Sale in the database
        List<Sale> saleList = saleRepository.findAll().collectList().block();
        assertThat(saleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamSale() throws Exception {
        int databaseSizeBeforeUpdate = saleRepository.findAll().collectList().block().size();
        sale.setId(longCount.incrementAndGet());

        // Create the Sale
        SaleDTO saleDTO = saleMapper.toDto(sale);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(saleDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Sale in the database
        List<Sale> saleList = saleRepository.findAll().collectList().block();
        assertThat(saleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteSale() {
        // Initialize the database
        saleRepository.save(sale).block();

        int databaseSizeBeforeDelete = saleRepository.findAll().collectList().block().size();

        // Delete the sale
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, sale.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Sale> saleList = saleRepository.findAll().collectList().block();
        assertThat(saleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
