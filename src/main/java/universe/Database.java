package universe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class Database {

    private Map<String, Table> tables;

    public Database() {
        tables = new HashMap<>();
    }

    public Table getTable(String tableName) {
        return tables.get(tableName);
    }

    public void createTable(String tableName, List<ColumnDefinition> columnDefinitions) {
        Table table = Table.builder().name(tableName).definitions(columnDefinitions).build();
        tables.put(tableName, table);
    }

}
