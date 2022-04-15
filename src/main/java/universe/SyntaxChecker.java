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
        boolean isValid = checkValidationOfColumnDefinitions(columnDefinitions);
        return isValid && !columnDefinitions.isEmpty();
    }

    private boolean checkValidationOfColumnDefinitions(List<ColumnDefinition> columnDefinitions) {
        return false;
    }

}
