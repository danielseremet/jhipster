package y.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import y.domain.Sale;

/**
 * Spring Data R2DBC repository for the Sale entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SaleRepository extends ReactiveCrudRepository<Sale, Long>, SaleRepositoryInternal {
    Flux<Sale> findAllBy(Pageable pageable);

    @Query("SELECT * FROM sale entity WHERE entity.car_id = :id")
    Flux<Sale> findByCar(Long id);

    @Query("SELECT * FROM sale entity WHERE entity.car_id IS NULL")
    Flux<Sale> findAllWhereCarIsNull();

    @Query("SELECT * FROM sale entity WHERE entity.customer_id = :id")
    Flux<Sale> findByCustomer(Long id);

    @Query("SELECT * FROM sale entity WHERE entity.customer_id IS NULL")
    Flux<Sale> findAllWhereCustomerIsNull();

    @Query("SELECT * FROM sale entity WHERE entity.employee_id = :id")
    Flux<Sale> findByEmployee(Long id);

    @Query("SELECT * FROM sale entity WHERE entity.employee_id IS NULL")
    Flux<Sale> findAllWhereEmployeeIsNull();

    @Override
    <S extends Sale> Mono<S> save(S entity);

    @Override
    Flux<Sale> findAll();

    @Override
    Mono<Sale> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface SaleRepositoryInternal {
    <S extends Sale> Mono<S> save(S entity);

    Flux<Sale> findAllBy(Pageable pageable);

    Flux<Sale> findAll();

    Mono<Sale> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Sale> findAllBy(Pageable pageable, Criteria criteria);
}
