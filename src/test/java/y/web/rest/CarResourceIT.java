package y.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static y.web.rest.TestUtil.sameNumber;

import java.math.BigDecimal;
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
import y.domain.Car;
import y.repository.CarRepository;
import y.repository.EntityManager;
import y.service.dto.CarDTO;
import y.service.mapper.CarMapper;

/**
 * Integration tests for the {@link CarResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CarResourceIT {

    private static final String DEFAULT_BRAND = "AAAAAAAAAA";
    private static final String UPDATED_BRAND = "BBBBBBBBBB";

    private static final String DEFAULT_MODEL = "AAAAAAAAAA";
    private static final String UPDATED_MODEL = "BBBBBBBBBB";

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    private static final Integer DEFAULT_MILEAGE = 1;
    private static final Integer UPDATED_MILEAGE = 2;

    private static final String DEFAULT_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_COLOR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cars";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CarMapper carMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Car car;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Car createEntity(EntityManager em) {
        Car car = new Car()
            .brand(DEFAULT_BRAND)
            .model(DEFAULT_MODEL)
            .year(DEFAULT_YEAR)
            .price(DEFAULT_PRICE)
            .mileage(DEFAULT_MILEAGE)
            .color(DEFAULT_COLOR);
        return car;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Car createUpdatedEntity(EntityManager em) {
        Car car = new Car()
            .brand(UPDATED_BRAND)
            .model(UPDATED_MODEL)
            .year(UPDATED_YEAR)
            .price(UPDATED_PRICE)
            .mileage(UPDATED_MILEAGE)
            .color(UPDATED_COLOR);
        return car;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Car.class).block();
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
        car = createEntity(em);
    }

    @Test
    void createCar() throws Exception {
        int databaseSizeBeforeCreate = carRepository.findAll().collectList().block().size();
        // Create the Car
        CarDTO carDTO = carMapper.toDto(car);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(carDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll().collectList().block();
        assertThat(carList).hasSize(databaseSizeBeforeCreate + 1);
        Car testCar = carList.get(carList.size() - 1);
        assertThat(testCar.getBrand()).isEqualTo(DEFAULT_BRAND);
        assertThat(testCar.getModel()).isEqualTo(DEFAULT_MODEL);
        assertThat(testCar.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testCar.getPrice()).isEqualByComparingTo(DEFAULT_PRICE);
        assertThat(testCar.getMileage()).isEqualTo(DEFAULT_MILEAGE);
        assertThat(testCar.getColor()).isEqualTo(DEFAULT_COLOR);
    }

    @Test
    void createCarWithExistingId() throws Exception {
        // Create the Car with an existing ID
        car.setId(1L);
        CarDTO carDTO = carMapper.toDto(car);

        int databaseSizeBeforeCreate = carRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(carDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll().collectList().block();
        assertThat(carList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkBrandIsRequired() throws Exception {
        int databaseSizeBeforeTest = carRepository.findAll().collectList().block().size();
        // set the field null
        car.setBrand(null);

        // Create the Car, which fails.
        CarDTO carDTO = carMapper.toDto(car);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(carDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Car> carList = carRepository.findAll().collectList().block();
        assertThat(carList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkModelIsRequired() throws Exception {
        int databaseSizeBeforeTest = carRepository.findAll().collectList().block().size();
        // set the field null
        car.setModel(null);

        // Create the Car, which fails.
        CarDTO carDTO = carMapper.toDto(car);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(carDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Car> carList = carRepository.findAll().collectList().block();
        assertThat(carList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkYearIsRequired() throws Exception {
        int databaseSizeBeforeTest = carRepository.findAll().collectList().block().size();
        // set the field null
        car.setYear(null);

        // Create the Car, which fails.
        CarDTO carDTO = carMapper.toDto(car);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(carDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Car> carList = carRepository.findAll().collectList().block();
        assertThat(carList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = carRepository.findAll().collectList().block().size();
        // set the field null
        car.setPrice(null);

        // Create the Car, which fails.
        CarDTO carDTO = carMapper.toDto(car);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(carDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Car> carList = carRepository.findAll().collectList().block();
        assertThat(carList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllCars() {
        // Initialize the database
        carRepository.save(car).block();

        // Get all the carList
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
            .value(hasItem(car.getId().intValue()))
            .jsonPath("$.[*].brand")
            .value(hasItem(DEFAULT_BRAND))
            .jsonPath("$.[*].model")
            .value(hasItem(DEFAULT_MODEL))
            .jsonPath("$.[*].year")
            .value(hasItem(DEFAULT_YEAR))
            .jsonPath("$.[*].price")
            .value(hasItem(sameNumber(DEFAULT_PRICE)))
            .jsonPath("$.[*].mileage")
            .value(hasItem(DEFAULT_MILEAGE))
            .jsonPath("$.[*].color")
            .value(hasItem(DEFAULT_COLOR));
    }

    @Test
    void getCar() {
        // Initialize the database
        carRepository.save(car).block();

        // Get the car
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, car.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(car.getId().intValue()))
            .jsonPath("$.brand")
            .value(is(DEFAULT_BRAND))
            .jsonPath("$.model")
            .value(is(DEFAULT_MODEL))
            .jsonPath("$.year")
            .value(is(DEFAULT_YEAR))
            .jsonPath("$.price")
            .value(is(sameNumber(DEFAULT_PRICE)))
            .jsonPath("$.mileage")
            .value(is(DEFAULT_MILEAGE))
            .jsonPath("$.color")
            .value(is(DEFAULT_COLOR));
    }

    @Test
    void getNonExistingCar() {
        // Get the car
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingCar() throws Exception {
        // Initialize the database
        carRepository.save(car).block();

        int databaseSizeBeforeUpdate = carRepository.findAll().collectList().block().size();

        // Update the car
        Car updatedCar = carRepository.findById(car.getId()).block();
        updatedCar
            .brand(UPDATED_BRAND)
            .model(UPDATED_MODEL)
            .year(UPDATED_YEAR)
            .price(UPDATED_PRICE)
            .mileage(UPDATED_MILEAGE)
            .color(UPDATED_COLOR);
        CarDTO carDTO = carMapper.toDto(updatedCar);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, carDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(carDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll().collectList().block();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
        Car testCar = carList.get(carList.size() - 1);
        assertThat(testCar.getBrand()).isEqualTo(UPDATED_BRAND);
        assertThat(testCar.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testCar.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testCar.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
        assertThat(testCar.getMileage()).isEqualTo(UPDATED_MILEAGE);
        assertThat(testCar.getColor()).isEqualTo(UPDATED_COLOR);
    }

    @Test
    void putNonExistingCar() throws Exception {
        int databaseSizeBeforeUpdate = carRepository.findAll().collectList().block().size();
        car.setId(longCount.incrementAndGet());

        // Create the Car
        CarDTO carDTO = carMapper.toDto(car);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, carDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(carDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll().collectList().block();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCar() throws Exception {
        int databaseSizeBeforeUpdate = carRepository.findAll().collectList().block().size();
        car.setId(longCount.incrementAndGet());

        // Create the Car
        CarDTO carDTO = carMapper.toDto(car);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(carDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll().collectList().block();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCar() throws Exception {
        int databaseSizeBeforeUpdate = carRepository.findAll().collectList().block().size();
        car.setId(longCount.incrementAndGet());

        // Create the Car
        CarDTO carDTO = carMapper.toDto(car);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(carDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll().collectList().block();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCarWithPatch() throws Exception {
        // Initialize the database
        carRepository.save(car).block();

        int databaseSizeBeforeUpdate = carRepository.findAll().collectList().block().size();

        // Update the car using partial update
        Car partialUpdatedCar = new Car();
        partialUpdatedCar.setId(car.getId());

        partialUpdatedCar.model(UPDATED_MODEL).year(UPDATED_YEAR).color(UPDATED_COLOR);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCar.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCar))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll().collectList().block();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
        Car testCar = carList.get(carList.size() - 1);
        assertThat(testCar.getBrand()).isEqualTo(DEFAULT_BRAND);
        assertThat(testCar.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testCar.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testCar.getPrice()).isEqualByComparingTo(DEFAULT_PRICE);
        assertThat(testCar.getMileage()).isEqualTo(DEFAULT_MILEAGE);
        assertThat(testCar.getColor()).isEqualTo(UPDATED_COLOR);
    }

    @Test
    void fullUpdateCarWithPatch() throws Exception {
        // Initialize the database
        carRepository.save(car).block();

        int databaseSizeBeforeUpdate = carRepository.findAll().collectList().block().size();

        // Update the car using partial update
        Car partialUpdatedCar = new Car();
        partialUpdatedCar.setId(car.getId());

        partialUpdatedCar
            .brand(UPDATED_BRAND)
            .model(UPDATED_MODEL)
            .year(UPDATED_YEAR)
            .price(UPDATED_PRICE)
            .mileage(UPDATED_MILEAGE)
            .color(UPDATED_COLOR);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCar.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCar))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll().collectList().block();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
        Car testCar = carList.get(carList.size() - 1);
        assertThat(testCar.getBrand()).isEqualTo(UPDATED_BRAND);
        assertThat(testCar.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testCar.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testCar.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
        assertThat(testCar.getMileage()).isEqualTo(UPDATED_MILEAGE);
        assertThat(testCar.getColor()).isEqualTo(UPDATED_COLOR);
    }

    @Test
    void patchNonExistingCar() throws Exception {
        int databaseSizeBeforeUpdate = carRepository.findAll().collectList().block().size();
        car.setId(longCount.incrementAndGet());

        // Create the Car
        CarDTO carDTO = carMapper.toDto(car);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, carDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(carDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll().collectList().block();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCar() throws Exception {
        int databaseSizeBeforeUpdate = carRepository.findAll().collectList().block().size();
        car.setId(longCount.incrementAndGet());

        // Create the Car
        CarDTO carDTO = carMapper.toDto(car);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(carDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll().collectList().block();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCar() throws Exception {
        int databaseSizeBeforeUpdate = carRepository.findAll().collectList().block().size();
        car.setId(longCount.incrementAndGet());

        // Create the Car
        CarDTO carDTO = carMapper.toDto(car);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(carDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll().collectList().block();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCar() {
        // Initialize the database
        carRepository.save(car).block();

        int databaseSizeBeforeDelete = carRepository.findAll().collectList().block().size();

        // Delete the car
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, car.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Car> carList = carRepository.findAll().collectList().block();
        assertThat(carList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
