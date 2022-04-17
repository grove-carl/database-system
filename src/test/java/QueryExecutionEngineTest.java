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
    void should_create_table_when_execute_create_table_statement_given_multiple_columns_are_defined() {
        String tableName = "user";
        String idColumnName = "id";
        String idColumnType = "Integer";
        String usernameColumnName = "username";
        String usernameColumnType = "String";
        String passwordColumnName = "password";
        String passwordColumnType = "String";
        List<ColumnDefinition> columnDefinitions = List.of(
                ColumnDefinition.builder().columnName(idColumnName).columnType(idColumnType).build(),
                ColumnDefinition.builder().columnName(usernameColumnName).columnType(usernameColumnType).build(),
                ColumnDefinition.builder().columnName(passwordColumnName).columnType(passwordColumnType).build()
        );

        queryExecutionEngine.execute(tableName, columnDefinitions);

        Table userTable = database.getTable("user");
        assertNotNull(userTable);

        List<ColumnDefinition> actualColumnDefinitions = userTable.getDefinitions();
        assertEquals(3, actualColumnDefinitions.size());
        assertEquals(idColumnName, actualColumnDefinitions.get(0).getColumnName());
        assertEquals(idColumnType, actualColumnDefinitions.get(0).getColumnType());
        assertEquals(usernameColumnName, actualColumnDefinitions.get(1).getColumnName());
        assertEquals(usernameColumnType, actualColumnDefinitions.get(1).getColumnType());
        assertEquals(passwordColumnName, actualColumnDefinitions.get(2).getColumnName());
        assertEquals(passwordColumnType, actualColumnDefinitions.get(2).getColumnType());
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

    @Test
    void should_drop_table_when_execute_drop_table_statement_given_multiple_table_exist() {
        String userTableName = "user";
        String userIdColumnName = "id";
        String userIdColumnType = "Integer";
        List<ColumnDefinition> userTableColumnDefinitions = List.of(
                ColumnDefinition.builder().columnName(userIdColumnName).columnType(userIdColumnType).build()
        );
        database.createTable(userTableName, userTableColumnDefinitions);

        String adminTableName = "admin";
        String adminNameColumnName = "name";
        String adminNameColumnType = "String";
        List<ColumnDefinition> adminTableColumnDefinitions = List.of(
                ColumnDefinition.builder().columnName(adminNameColumnName).columnType(adminNameColumnType).build()
        );
        database.createTable(adminTableName, adminTableColumnDefinitions);

        queryExecutionEngine.execute(userTableName);

        assertEquals(1, database.getTables().size());
    }

}