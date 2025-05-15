package y.repository.rowmapper;

import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import y.domain.Sale;

/**
 * Converter between {@link Row} to {@link Sale}, with proper type conversions.
 */
@Service
public class SaleRowMapper implements BiFunction<Row, String, Sale> {

    private final ColumnConverter converter;

    public SaleRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Sale} stored in the database.
     */
    @Override
    public Sale apply(Row row, String prefix) {
        Sale entity = new Sale();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setSaleDate(converter.fromRow(row, prefix + "_sale_date", LocalDate.class));
        entity.setSalePrice(converter.fromRow(row, prefix + "_sale_price", BigDecimal.class));
        entity.setQuantity(converter.fromRow(row, prefix + "_quantity", Integer.class));
        entity.setCarId(converter.fromRow(row, prefix + "_car_id", Long.class));
        entity.setCustomerId(converter.fromRow(row, prefix + "_customer_id", Long.class));
        entity.setEmployeeId(converter.fromRow(row, prefix + "_employee_id", Long.class));
        return entity;
    }
}
