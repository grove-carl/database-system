package universe;

import java.util.List;
import universe.config.ValidColumnTypes;
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
        List<String> validColumnTypes = ValidColumnTypes.get();
        for (ColumnDefinition columnDefinition : columnDefinitions) {
            if (!validColumnTypes.contains(columnDefinition.getColumnType())) {
                return false;
            }
        }
        return true;
    }

}
