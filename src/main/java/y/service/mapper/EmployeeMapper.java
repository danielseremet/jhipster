package y.service.mapper;

import org.mapstruct.*;
import y.domain.Employee;
import y.service.dto.EmployeeDTO;

/**
 * Mapper for the entity {@link Employee} and its DTO {@link EmployeeDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmployeeMapper extends EntityMapper<EmployeeDTO, Employee> {}
