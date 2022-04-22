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

    public void check(String statement) {
        if (isCreateTableStatement(statement)) {
            String tableName = CreateTableStatementUtils.extractTableNameFromCreateTableStatement(statement);
            checkValidationOfTableName(tableName);

            List<ColumnDefinition> columnDefinitions =
                    CreateTableStatementUtils.extractColumnDefinitionsFromCreateTableStatement(statement);
            checkValidationOfColumnDefinitions(columnDefinitions);
        } else if (isDropTableStatement(statement)) {
            String tableName = CreateTableStatementUtils.extractTableNameFromDropTableStatement(statement);
            checkIsTableExist(tableName);
        } else if (isAlterTableStatement(statement)) {
            String tableName = CreateTableStatementUtils.extractTableNameFromAlterTableStatement(statement);
            checkIsTableNameEmpty(tableName);
        }
    }

    private boolean isCreateTableStatement(String statement) {
        return statement.startsWith("create table");
    }

    private boolean isDropTableStatement(String statement) {
        return statement.startsWith("drop table");
    }

    private boolean isAlterTableStatement(String statement) {
        return statement.startsWith("alter table");
    }

    private void checkIsTableExist(String tableName) {
        if (database.getTable(tableName) == null) {
            throw new Error(ErrorCollection.TABLE_NOT_EXIST, tableName);
        }
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
