package y.service.mapper;

import org.mapstruct.*;
import y.domain.Car;
import y.service.dto.CarDTO;

/**
 * Mapper for the entity {@link Car} and its DTO {@link CarDTO}.
 */
@Mapper(componentModel = "spring")
public interface CarMapper extends EntityMapper<CarDTO, Car> {}
