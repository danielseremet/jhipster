package y.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import y.domain.Car;

/**
 * Converter between {@link Row} to {@link Car}, with proper type conversions.
 */
@Service
public class CarRowMapper implements BiFunction<Row, String, Car> {

    private final ColumnConverter converter;

    public CarRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Car} stored in the database.
     */
    @Override
    public Car apply(Row row, String prefix) {
        Car entity = new Car();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setBrand(converter.fromRow(row, prefix + "_brand", String.class));
        entity.setModel(converter.fromRow(row, prefix + "_model", String.class));
        entity.setYear(converter.fromRow(row, prefix + "_year", Integer.class));
        entity.setPrice(converter.fromRow(row, prefix + "_price", BigDecimal.class));
        entity.setMileage(converter.fromRow(row, prefix + "_mileage", Integer.class));
        entity.setColor(converter.fromRow(row, prefix + "_color", String.class));
        entity.setAvailable(converter.fromRow(row, prefix + "_available", Boolean.class));
        return entity;
    }
}
