package y.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import y.domain.Car;

/**
 * Spring Data R2DBC repository for the Car entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CarRepository extends ReactiveCrudRepository<Car, Long>, CarRepositoryInternal {
    Flux<Car> findAllBy(Pageable pageable);

    @Override
    <S extends Car> Mono<S> save(S entity);

    @Override
    Flux<Car> findAll();

    @Override
    Mono<Car> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);

    @Query(
        """
        Select * From car c
        Join sale s On c.id = s.car_id
        Join customer cu On s.customer_id = cu.id
        Where cu.first_name = :firstName And cu.last_name = :lastName
        """
    )
    Flux<Car> findCarByCostumer(@Param("firstName") String firstName, @Param("lastName") String lastName);

    @Query(
        """
        SELECT * FROM car
        WHERE available=true
        """
    )
    Flux<Car> findAllAvailableCars();
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Car> findAllBy(Pageable pageable, Criteria criteria);
}
