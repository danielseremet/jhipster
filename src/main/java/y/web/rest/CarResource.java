package y.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.ForwardedHeaderUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;
import y.repository.CarRepository;
import y.service.CarService;
import y.service.dto.CarDTO;
import y.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link y.domain.Car}.
 */
@RestController
@RequestMapping("/api/cars")
public class CarResource {

    private final Logger log = LoggerFactory.getLogger(CarResource.class);

    private static final String ENTITY_NAME = "car";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CarService carService;

    private final CarRepository carRepository;

    public CarResource(CarService carService, CarRepository carRepository) {
        this.carService = carService;
        this.carRepository = carRepository;
    }

    /**
     * {@code POST  /cars} : Create a new car.
     *
     * @param carDTO the carDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new carDTO, or with status {@code 400 (Bad Request)} if the car has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<CarDTO>> createCar(@Valid @RequestBody CarDTO carDTO) throws URISyntaxException {
        log.debug("REST request to save Car : {}", carDTO);
        if (carDTO.getId() != null) {
            throw new BadRequestAlertException("A new car cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return carService
            .save(carDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/cars/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /cars/:id} : Updates an existing car.
     *
     * @param id the id of the carDTO to save.
     * @param carDTO the carDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated carDTO,
     * or with status {@code 400 (Bad Request)} if the carDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the carDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<CarDTO>> updateCar(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CarDTO carDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Car : {}, {}", id, carDTO);
        if (carDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, carDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return carRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return carService
                    .update(carDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /cars/:id} : Partial updates given fields of an existing car, field will ignore if it is null
     *
     * @param id the id of the carDTO to save.
     * @param carDTO the carDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated carDTO,
     * or with status {@code 400 (Bad Request)} if the carDTO is not valid,
     * or with status {@code 404 (Not Found)} if the carDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the carDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<CarDTO>> partialUpdateCar(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CarDTO carDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Car partially : {}, {}", id, carDTO);
        if (carDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, carDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return carRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<CarDTO> result = carService.partialUpdate(carDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /cars} : get all the cars.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cars in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<CarDTO>>> getAllCars(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of Cars");
        return carService
            .countAll()
            .zipWith(carService.findAll(pageable).collectList())
            .map(countWithEntities ->
                ResponseEntity
                    .ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /cars/:id} : get the "id" car.
     *
     * @param id the id of the carDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the carDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<CarDTO>> getCar(@PathVariable("id") Long id) {
        log.debug("REST request to get Car : {}", id);
        Mono<CarDTO> carDTO = carService.findOne(id);
        return ResponseUtil.wrapOrNotFound(carDTO);
    }

    @GetMapping("/availableCars")
    public Flux<CarDTO> getAvailableCars() {
        log.debug("REST request to get available cars");
        return carService.getAllAvailableCars();
    }

    /**
     * {@code DELETE  /cars/:id} : delete the "id" car.
     *
     * @param id the id of the carDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteCar(@PathVariable("id") Long id) {
        log.debug("REST request to delete Car : {}", id);
        return carService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity
                        .noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }

    //TODO Provide me an working method that will return the cars based on user's customerName and surname
    @GetMapping("/customer/{customerName}")
    public Flux<CarDTO> getByCostumerName(@PathVariable String customerName) {
        log.debug("REST request to get costumers {} Cars", customerName);
        return carService.findCostumersCars(customerName);
    }
}
