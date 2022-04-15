package universe;

import java.util.List;
import universe.util.CreateTableStatementUtils;

public class QueryExecutionEngine {

    private final Database database;

    public QueryExecutionEngine() {
        this.database = DatabaseFactory.getDatabase();
    }

    public void execute(String statement) {
        executeCreateTableStatement(statement);
    }

    private void executeCreateTableStatement(String statement) {
        String tableName = CreateTableStatementUtils.extractTableNameFromCreateTableStatement(statement);
        List<ColumnDefinition> columnDefinitions = CreateTableStatementUtils.extractColumnDefinitionsFromCreateTableStatement(statement);
        database.createTable(tableName, columnDefinitions);
    }

}
