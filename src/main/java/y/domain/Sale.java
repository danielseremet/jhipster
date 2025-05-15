package y.domain;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Sale.
 */
@Table("sale")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Sale implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("sale_date")
    private LocalDate saleDate;

    @NotNull(message = "must not be null")
    @Column("sale_price")
    private BigDecimal salePrice;

    @NotNull(message = "must not be null")
    @Column("quantity")
    private Integer quantity;

    @Transient
    private Car car;

    @Transient
    private Customer customer;

    @Transient
    private Employee employee;

    @Column("car_id")
    private Long carId;

    @Column("customer_id")
    private Long customerId;

    @Column("employee_id")
    private Long employeeId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Sale id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getSaleDate() {
        return this.saleDate;
    }

    public Sale saleDate(LocalDate saleDate) {
        this.setSaleDate(saleDate);
        return this;
    }

    public void setSaleDate(LocalDate saleDate) {
        this.saleDate = saleDate;
    }

    public BigDecimal getSalePrice() {
        return this.salePrice;
    }

    public Sale salePrice(BigDecimal salePrice) {
        this.setSalePrice(salePrice);
        return this;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice != null ? salePrice.stripTrailingZeros() : null;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public Sale quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Car getCar() {
        return this.car;
    }

    public void setCar(Car car) {
        this.car = car;
        this.carId = car != null ? car.getId() : null;
    }

    public Sale car(Car car) {
        this.setCar(car);
        return this;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        this.customerId = customer != null ? customer.getId() : null;
    }

    public Sale customer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
        this.employeeId = employee != null ? employee.getId() : null;
    }

    public Sale employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    public Long getCarId() {
        return this.carId;
    }

    public void setCarId(Long car) {
        this.carId = car;
    }

    public Long getCustomerId() {
        return this.customerId;
    }

    public void setCustomerId(Long customer) {
        this.customerId = customer;
    }

    public Long getEmployeeId() {
        return this.employeeId;
    }

    public void setEmployeeId(Long employee) {
        this.employeeId = employee;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sale)) {
            return false;
        }
        return getId() != null && getId().equals(((Sale) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Sale{" +
            "id=" + getId() +
            ", saleDate='" + getSaleDate() + "'" +
            ", salePrice=" + getSalePrice() +
            ", quantity=" + getQuantity() +
            "}";
    }
}
