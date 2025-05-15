package y.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class CarMapperTest {

    private CarMapper carMapper;

    @BeforeEach
    public void setUp() {
        carMapper = new CarMapperImpl();
    }
}
