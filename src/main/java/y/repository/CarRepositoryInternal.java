package y.repository;

import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import y.domain.Car;

public interface CarRepositoryInternal {
    <S extends Car> Mono<S> save(S entity);

    Flux<Car> findAllBy(Pageable pageable);

    Flux<Car> findAll();

    Mono<Car> findById(Long id);

    Flux<Car> findCarByCostumer(String firstN, String lastN);
}
