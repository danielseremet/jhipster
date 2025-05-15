package y.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import y.repository.CarRepository;
import y.service.dto.CarDTO;
import y.service.mapper.CarMapper;

/**
 * Service Implementation for managing {@link y.domain.Car}.
 */
@Service
@Transactional
public class CarService {

    private final Logger log = LoggerFactory.getLogger(CarService.class);

    private final CarRepository carRepository;

    private final CarMapper carMapper;

    public CarService(CarRepository carRepository, CarMapper carMapper) {
        this.carRepository = carRepository;
        this.carMapper = carMapper;
    }

    /**
     * Save a car.
     *
     * @param carDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CarDTO> save(CarDTO carDTO) {
        log.debug("Request to save Car : {}", carDTO);
        return carRepository.save(carMapper.toEntity(carDTO)).map(carMapper::toDto);
    }

    /**
     * Update a car.
     *
     * @param carDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CarDTO> update(CarDTO carDTO) {
        log.debug("Request to update Car : {}", carDTO);
        return carRepository.save(carMapper.toEntity(carDTO)).map(carMapper::toDto);
    }

    /**
     * Partially update a car.
     *
     * @param carDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<CarDTO> partialUpdate(CarDTO carDTO) {
        log.debug("Request to partially update Car : {}", carDTO);

        return carRepository
            .findById(carDTO.getId())
            .map(existingCar -> {
                carMapper.partialUpdate(existingCar, carDTO);

                return existingCar;
            })
            .flatMap(carRepository::save)
            .map(carMapper::toDto);
    }

    /**
     * Get all the cars.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<CarDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Cars");
        return carRepository.findAllBy(pageable).map(carMapper::toDto);
    }

    /**
     * Returns the number of cars available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return carRepository.count();
    }

    /**
     * Get one car by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<CarDTO> findOne(Long id) {
        log.debug("Request to get Car : {}", id);
        return carRepository.findById(id).map(carMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Flux<CarDTO> findCostumersCars(String name) {
        log.debug("Request to get CostumersCars : {}", name);
        String[] names = name.split(" ");
        return carRepository.findCarByCostumer(names[0], names[1]).map(carMapper::toDto);
    }

    /**
     * Delete the car by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Car : {}", id);
        return carRepository.deleteById(id);
    }
}
