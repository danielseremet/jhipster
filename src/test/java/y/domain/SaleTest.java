package y.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static y.domain.CarTestSamples.*;
import static y.domain.CustomerTestSamples.*;
import static y.domain.EmployeeTestSamples.*;
import static y.domain.SaleTestSamples.*;

import org.junit.jupiter.api.Test;
import y.web.rest.TestUtil;

class SaleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sale.class);
        Sale sale1 = getSaleSample1();
        Sale sale2 = new Sale();
        assertThat(sale1).isNotEqualTo(sale2);

        sale2.setId(sale1.getId());
        assertThat(sale1).isEqualTo(sale2);

        sale2 = getSaleSample2();
        assertThat(sale1).isNotEqualTo(sale2);
    }

    @Test
    void carTest() throws Exception {
        Sale sale = getSaleRandomSampleGenerator();
        Car carBack = getCarRandomSampleGenerator();

        sale.setCar(carBack);
        assertThat(sale.getCar()).isEqualTo(carBack);

        sale.car(null);
        assertThat(sale.getCar()).isNull();
    }

    @Test
    void customerTest() throws Exception {
        Sale sale = getSaleRandomSampleGenerator();
        Customer customerBack = getCustomerRandomSampleGenerator();

        sale.setCustomer(customerBack);
        assertThat(sale.getCustomer()).isEqualTo(customerBack);

        sale.customer(null);
        assertThat(sale.getCustomer()).isNull();
    }

    @Test
    void employeeTest() throws Exception {
        Sale sale = getSaleRandomSampleGenerator();
        Employee employeeBack = getEmployeeRandomSampleGenerator();

        sale.setEmployee(employeeBack);
        assertThat(sale.getEmployee()).isEqualTo(employeeBack);

        sale.employee(null);
        assertThat(sale.getEmployee()).isNull();
    }
}
