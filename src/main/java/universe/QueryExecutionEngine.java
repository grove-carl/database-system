package universe;

import java.util.List;

public class QueryExecutionEngine {

    private final Database database;

    public QueryExecutionEngine() {
        this.database = DatabaseFactory.getDatabase();
    }

    public void execute(String tableName, List<ColumnDefinition> columnDefinitions) {
        database.createTable(tableName, columnDefinitions);
    }
}
