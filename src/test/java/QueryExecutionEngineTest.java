import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import universe.Database;
import universe.DatabaseFactory;
import universe.QueryExecutionEngine;
import universe.Table;
import java.util.List;
import org.junit.jupiter.api.Test;
import universe.ColumnDefinition;

class QueryExecutionEngineTest {

    private QueryExecutionEngine queryExecutionEngine;

    private final Database database;

    @BeforeEach
    public void setUp() {
        queryExecutionEngine = new QueryExecutionEngine();
    }

    public QueryExecutionEngineTest() {
        this.database = DatabaseFactory.getDatabase();
    }

    @Test
    void should_create_table_when_execute_create_table_statement_given_single_column_is_defined() {
        String tableName = "user";
        String columnName = "id";
        String columnType = "Integer";
        List<ColumnDefinition> columnDefinitions =
                List.of(ColumnDefinition.builder().columnName(columnName).columnType(columnType).build());

        queryExecutionEngine.execute(tableName, columnDefinitions);

        Table userTable = database.getTable("user");
        assertNotNull(userTable);

        List<ColumnDefinition> actualColumnDefinitions = userTable.getDefinitions();
        assertEquals(1, actualColumnDefinitions.size());
        assertEquals("id", actualColumnDefinitions.get(0).getColumnName());
        assertEquals("Integer", actualColumnDefinitions.get(0).getColumnType());
    }

    @Test
    void should_drop_table_when_execute_drop_table_statement_given_one_table_exist() {
        String tableName = "user";
        String columnName = "id";
        String columnType = "Integer";
        List<ColumnDefinition> columnDefinitions =
                List.of(ColumnDefinition.builder().columnName(columnName).columnType(columnType).build());
        database.createTable(tableName, columnDefinitions);

        queryExecutionEngine.execute(tableName);

        assertEquals(0, database.getTables().size());
    }

}