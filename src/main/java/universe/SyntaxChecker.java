package universe;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import universe.config.ValidColumnTypes;
import universe.exception.Error;
import universe.exception.ErrorCollection;
import universe.util.CreateTableStatementUtils;

public class SyntaxChecker {

    private final Database database;

    public SyntaxChecker() {
        this.database = DatabaseFactory.getDatabase();
    }

    public boolean isValid(String statement) {
        String tableName = CreateTableStatementUtils.extractTableNameFromCreateTableStatement(statement);
        checkValidationOfTableName(tableName);

        List<ColumnDefinition> columnDefinitions =
                CreateTableStatementUtils.extractColumnDefinitionsFromCreateTableStatement(statement);
        checkValidationOfColumnDefinitions(columnDefinitions);
        return true;
    }

    private void checkValidationOfTableName(String tableName) {
        checkIsTableNameEmpty(tableName);
        checkIsTableNameDuplicate(tableName);
    }

    private void checkIsTableNameEmpty(String tableName) {
        if (tableName.isEmpty()) {
            throw new Error(ErrorCollection.MISSING_TABLE_NAME);
        }
    }

    private void checkIsTableNameDuplicate(String tableName) {
        if (database.getTable(tableName) != null) {
            throw new Error(ErrorCollection.DUPLICATE_TABLE_NAME, tableName);
        }
    }

    private void checkValidationOfColumnDefinitions(List<ColumnDefinition> columnDefinitions) {
        checkIsColumnDefinitionEmpty(columnDefinitions);
        checkColumnNameDuplication(columnDefinitions);
        checkColumnTypeValidation(columnDefinitions);
    }

    private void checkIsColumnDefinitionEmpty(List<ColumnDefinition> columnDefinitions) {
        if (columnDefinitions.isEmpty()) {
            throw new Error(ErrorCollection.EMPTY_COLUMN_DEFINITION);
        }
    }

    private void checkColumnNameDuplication(final List<ColumnDefinition> columnDefinitions) {
        Set<String> columnNameSet = new HashSet<>();
        for (ColumnDefinition columnDefinition : columnDefinitions) {
            if (columnNameSet.contains(columnDefinition.getColumnName())) {
                throw new Error(ErrorCollection.DUPLICATE_COLUMN_NAME, columnDefinition.getColumnName());
            } else {
                columnNameSet.add(columnDefinition.getColumnName());
            }
        }
    }

    private void checkColumnTypeValidation(final List<ColumnDefinition> columnDefinitions) {
        List<String> validColumnTypes = ValidColumnTypes.get();
        for (ColumnDefinition columnDefinition : columnDefinitions) {
            if (!validColumnTypes.contains(columnDefinition.getColumnType())) {
                throw new Error(ErrorCollection.UNSUPPORTED_COLUMN_TYPE);
            }
        }
    }

}
