package y.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class SaleSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("sale_date", table, columnPrefix + "_sale_date"));
        columns.add(Column.aliased("sale_price", table, columnPrefix + "_sale_price"));
        columns.add(Column.aliased("quantity", table, columnPrefix + "_quantity"));

        columns.add(Column.aliased("car_id", table, columnPrefix + "_car_id"));
        columns.add(Column.aliased("customer_id", table, columnPrefix + "_customer_id"));
        columns.add(Column.aliased("employee_id", table, columnPrefix + "_employee_id"));
        return columns;
    }
}
