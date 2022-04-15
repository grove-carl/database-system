package universe;

import java.util.List;
import universe.util.CreateTableStatementUtils;

public class SyntaxChecker {

    public boolean isValid(String statement) {
        String tableName = CreateTableStatementUtils.extractTableNameFromCreateTableStatement(statement);
        if (tableName.isEmpty()) {
            return false;
        }
        List<ColumnDefinition> columnDefinitions =
                CreateTableStatementUtils.extractColumnDefinitionsFromCreateTableStatement(statement);
        return !columnDefinitions.isEmpty();
    }

}
