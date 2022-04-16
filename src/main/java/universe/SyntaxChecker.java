package universe;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import universe.config.ValidColumnTypes;
import universe.util.CreateTableStatementUtils;

public class SyntaxChecker {

    private final Database database;

    public SyntaxChecker() {
        this.database = DatabaseFactory.getDatabase();
    }

    public boolean isValid(String statement) {
        String tableName = CreateTableStatementUtils.extractTableNameFromCreateTableStatement(statement);
        boolean isTableNameValid = checkValidationOfTableName(tableName);

        List<ColumnDefinition> columnDefinitions =
                CreateTableStatementUtils.extractColumnDefinitionsFromCreateTableStatement(statement);
        boolean isColumnDefinitionsValid = checkValidationOfColumnDefinitions(columnDefinitions);

        return isTableNameValid && isColumnDefinitionsValid;
    }

    private boolean checkValidationOfTableName(String tableName) {
        return !isTableNameEmpty(tableName) && !isTableNameDuplicate(tableName);
    }

    private boolean isTableNameEmpty(String tableName) {
        return tableName.isEmpty();
    }

    private boolean isTableNameDuplicate(String tableName) {
        return database.getTable(tableName) != null;
    }

    private boolean checkValidationOfColumnDefinitions(List<ColumnDefinition> columnDefinitions) {
        boolean isColumnDefinitionsEmpty = checkIsColumnDefinitionEmpty(columnDefinitions);
        boolean isColumnNameDuplicated = checkColumnNameDuplication(columnDefinitions);
        boolean isColumnTypeValid = checkColumnTypeValidation(columnDefinitions);

        return !isColumnDefinitionsEmpty && !isColumnNameDuplicated && isColumnTypeValid;
    }

    private boolean checkIsColumnDefinitionEmpty(List<ColumnDefinition> columnDefinitions) {
        return columnDefinitions.isEmpty();
    }

    private boolean checkColumnNameDuplication(final List<ColumnDefinition> columnDefinitions) {
        Set<String> columnNameSet = new HashSet<>();
        for (ColumnDefinition columnDefinition : columnDefinitions) {
            if (columnNameSet.contains(columnDefinition.getColumnName())) {
                return true;
            } else {
                columnNameSet.add(columnDefinition.getColumnName());
            }
        }
        return false;
    }

    private boolean checkColumnTypeValidation(final List<ColumnDefinition> columnDefinitions) {
        List<String> validColumnTypes = ValidColumnTypes.get();
        for (ColumnDefinition columnDefinition : columnDefinitions) {
            if (!validColumnTypes.contains(columnDefinition.getColumnType())) {
                return false;
            }
        }
        return true;
    }

}
