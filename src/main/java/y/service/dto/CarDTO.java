package y.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link y.domain.Car} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CarDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String brand;

    @NotNull(message = "must not be null")
    private String model;

    @NotNull(message = "must not be null")
    private Integer year;

    @NotNull(message = "must not be null")
    private BigDecimal price;

    private Integer mileage;

    private String color;

    private Boolean available;

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getMileage() {
        return mileage;
    }

    public void setMileage(Integer mileage) {
        this.mileage = mileage;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CarDTO)) {
            return false;
        }

        CarDTO carDTO = (CarDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, carDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CarDTO{" +
            "id=" + getId() +
            ", brand='" + getBrand() + "'" +
            ", model='" + getModel() + "'" +
            ", year=" + getYear() +
            ", price=" + getPrice() +
            ", mileage=" + getMileage() +
            ", color='" + getColor() + "'" +
            ", available=" + getAvailable() +
            "}";
    }
}
