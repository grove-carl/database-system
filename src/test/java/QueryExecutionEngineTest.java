import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        String statement = "create table user (id Integer);";
        QueryExecutionEngine queryExecutionEngine = new QueryExecutionEngine();

        queryExecutionEngine.execute(statement);

        Table userTable = database.getTable("user");
        assertNotNull(userTable);

        List<ColumnDefinition> columnDefinitions = userTable.getDefinitions();
        assertEquals(1, columnDefinitions.size());
        assertEquals("id", columnDefinitions.get(0).getColumnName());
        assertEquals("Integer", columnDefinitions.get(0).getColumnType());
    }

}