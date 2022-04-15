import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import universe.Database;
import universe.DatabaseFactory;
import universe.QueryExecutionEngine;
import universe.Table;
import java.util.List;
import org.junit.jupiter.api.Test;
import universe.ColumnDefinition;

class QueryExecutionEngineTest {

    private final Database database;

    public QueryExecutionEngineTest() {
        this.database = DatabaseFactory.getDatabase();
    }

    @Test
    void should_create_table_when_execute_create_table_statement_given_single_column_is_defined() {
        String tableName = "user";
        List<ColumnDefinition> columnDefinitions = new ArrayList<>();
        String columnName = "id";
        String columnType = "Integer";
        columnDefinitions.add(ColumnDefinition.builder().columnName(columnName).columnType(columnType).build());

        QueryExecutionEngine queryExecutionEngine = new QueryExecutionEngine();
        queryExecutionEngine.execute(tableName, columnDefinitions);

        Table userTable = database.getTable("user");
        assertNotNull(userTable);

        List<ColumnDefinition> actualColumnDefinitions = userTable.getDefinitions();
        assertEquals(1, actualColumnDefinitions.size());
        assertEquals("id", actualColumnDefinitions.get(0).getColumnName());
        assertEquals("Integer", actualColumnDefinitions.get(0).getColumnType());
    }

}