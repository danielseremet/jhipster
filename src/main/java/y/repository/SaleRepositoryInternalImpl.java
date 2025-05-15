package y.repository;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import y.domain.Sale;
import y.repository.rowmapper.CarRowMapper;
import y.repository.rowmapper.CustomerRowMapper;
import y.repository.rowmapper.EmployeeRowMapper;
import y.repository.rowmapper.SaleRowMapper;

/**
 * Spring Data R2DBC custom repository implementation for the Sale entity.
 */
@SuppressWarnings("unused")
class SaleRepositoryInternalImpl extends SimpleR2dbcRepository<Sale, Long> implements SaleRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final CarRowMapper carMapper;
    private final CustomerRowMapper customerMapper;
    private final EmployeeRowMapper employeeMapper;
    private final SaleRowMapper saleMapper;

    private static final Table entityTable = Table.aliased("sale", EntityManager.ENTITY_ALIAS);
    private static final Table carTable = Table.aliased("car", "car");
    private static final Table customerTable = Table.aliased("customer", "customer");
    private static final Table employeeTable = Table.aliased("employee", "employee");

    public SaleRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        CarRowMapper carMapper,
        CustomerRowMapper customerMapper,
        EmployeeRowMapper employeeMapper,
        SaleRowMapper saleMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Sale.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.carMapper = carMapper;
        this.customerMapper = customerMapper;
        this.employeeMapper = employeeMapper;
        this.saleMapper = saleMapper;
    }

    @Override
    public Flux<Sale> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Sale> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = SaleSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(CarSqlHelper.getColumns(carTable, "car"));
        columns.addAll(CustomerSqlHelper.getColumns(customerTable, "customer"));
        columns.addAll(EmployeeSqlHelper.getColumns(employeeTable, "employee"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(carTable)
            .on(Column.create("car_id", entityTable))
            .equals(Column.create("id", carTable))
            .leftOuterJoin(customerTable)
            .on(Column.create("customer_id", entityTable))
            .equals(Column.create("id", customerTable))
            .leftOuterJoin(employeeTable)
            .on(Column.create("employee_id", entityTable))
            .equals(Column.create("id", employeeTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Sale.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Sale> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Sale> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private Sale process(Row row, RowMetadata metadata) {
        Sale entity = saleMapper.apply(row, "e");
        entity.setCar(carMapper.apply(row, "car"));
        entity.setCustomer(customerMapper.apply(row, "customer"));
        entity.setEmployee(employeeMapper.apply(row, "employee"));
        return entity;
    }

    @Override
    public <S extends Sale> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
