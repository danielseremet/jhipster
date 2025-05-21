package y.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import y.repository.CarRepository;
import y.repository.SaleRepository;
import y.service.dto.SaleDTO;
import y.service.mapper.SaleMapper;
import y.web.rest.errors.BadRequestAlertException;

/**
 * Service Implementation for managing {@link y.domain.Sale}.
 */
@Service
@Transactional
public class SaleService {

    private final Logger log = LoggerFactory.getLogger(SaleService.class);

    private final SaleRepository saleRepository;
    private final CarService carService;
    private final SaleMapper saleMapper;

    public SaleService(SaleRepository saleRepository, CarService carService, SaleMapper saleMapper) {
        this.saleRepository = saleRepository;
        this.carService = carService;
        this.saleMapper = saleMapper;
    }

    /**
     * Save a sale.
     *
     * @param saleDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<SaleDTO> save(SaleDTO saleDTO) {
        log.debug("Request to save Sale : {}", saleDTO);
        return carService
            .isAvailable(saleDTO)
            .filter(Boolean::booleanValue)
            .switchIfEmpty(Mono.error(new BadRequestAlertException("Car is not available for sale", "Car", "car.sold")))
            .flatMap(available -> carService.updateCarAsSold(saleDTO))
            .flatMap(car -> saleRepository.save(saleMapper.toEntity(saleDTO)))
            .map(saleMapper::toDto);
    }

    /**
     * Update a sale.
     *
     * @param saleDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<SaleDTO> update(SaleDTO saleDTO) {
        log.debug("Request to update Sale : {}", saleDTO);
        carService.updateCarAsSold(saleDTO);
        return saleRepository.save(saleMapper.toEntity(saleDTO)).map(saleMapper::toDto);
    }

    /**
     * Partially update a sale.
     *
     * @param saleDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<SaleDTO> partialUpdate(SaleDTO saleDTO) {
        log.debug("Request to partially update Sale : {}", saleDTO);
        return saleRepository
            .findById(saleDTO.getId())
            .map(existingSale -> {
                saleMapper.partialUpdate(existingSale, saleDTO);

                return existingSale;
            })
            .flatMap(saleRepository::save)
            .map(saleMapper::toDto);
    }

    /**
     * Get all the sales.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<SaleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Sales");
        return saleRepository.findAllBy(pageable).map(saleMapper::toDto);
    }

    /**
     * Returns the number of sales available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return saleRepository.count();
    }

    /**
     * Get one sale by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<SaleDTO> findOne(Long id) {
        log.debug("Request to get Sale : {}", id);
        return saleRepository.findById(id).map(saleMapper::toDto);
    }

    /**
     * Delete the sale by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Sale : {}", id);
        return saleRepository.deleteById(id);
    }
}
