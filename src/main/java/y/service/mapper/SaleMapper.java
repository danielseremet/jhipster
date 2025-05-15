package y.service.mapper;

import org.mapstruct.*;
import y.domain.Car;
import y.domain.Customer;
import y.domain.Employee;
import y.domain.Sale;
import y.service.dto.CarDTO;
import y.service.dto.CustomerDTO;
import y.service.dto.EmployeeDTO;
import y.service.dto.SaleDTO;

/**
 * Mapper for the entity {@link Sale} and its DTO {@link SaleDTO}.
 */
@Mapper(componentModel = "spring")
public interface SaleMapper extends EntityMapper<SaleDTO, Sale> {
    @Mapping(target = "car", source = "car", qualifiedByName = "carId")
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerId")
    @Mapping(target = "employee", source = "employee", qualifiedByName = "employeeId")
    SaleDTO toDto(Sale s);

    @Named("carId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CarDTO toDtoCarId(Car car);

    @Named("customerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CustomerDTO toDtoCustomerId(Customer customer);

    @Named("employeeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EmployeeDTO toDtoEmployeeId(Employee employee);
}
