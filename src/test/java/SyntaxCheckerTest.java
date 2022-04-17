import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import universe.ColumnDefinition;
import universe.Database;
import universe.DatabaseFactory;
import universe.SyntaxChecker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import universe.exception.Error;
import universe.exception.ErrorCollection;

class SyntaxCheckerTest {

    private SyntaxChecker syntaxChecker;

    private final Database database = DatabaseFactory.getDatabase();

    @BeforeEach
    void setUp() {
        syntaxChecker = new SyntaxChecker();
    }

    @AfterEach
    void tearDown() {
        database.deleteAllTables();
    }

    @Test
    void should_return_true_when_create_table_given_one_column_with_column_name_and_data_type() {
        String statement = "create table user (id Integer);";
        boolean expectedResult = true;
        assertValidationOfStatement(statement, expectedResult);
    }

    @Test
    void should_throw_exception_1002_when_create_table_given_column_name_is_missing() {
        String statement = "create table user (Integer);";
        Error error = new Error(ErrorCollection.UNSUPPORTED_COLUMN_TYPE);
        assertThrows(RuntimeException.class, () -> syntaxChecker.isValid(statement), error.toString());
    }

    @Test
    void should_throw_exception_1002_when_create_table_given_column_type_is_missing() {
        String statement = "create table user (id);";
        Error error = new Error(ErrorCollection.UNSUPPORTED_COLUMN_TYPE);
        assertThrows(RuntimeException.class, () -> syntaxChecker.isValid(statement), error.toString());
    }

    @Test
    void should_throw_exception_1001_when_create_table_given_table_name_is_missing() {
        String statement = "create table (id Integer);";
        Error error = new Error(ErrorCollection.MISSING_TABLE_NAME);
        assertThrows(RuntimeException.class, () -> syntaxChecker.isValid(statement), error.toString());
    }

    @Test
    void should_return_true_when_create_table_given_multiple_columns_are_inputted() {
        String statement = "create table user (id Integer, username String, password String);";
        boolean expectedResult = true;
        assertValidationOfStatement(statement, expectedResult);
    }

    @Test
    void should_throw_exception_1002_when_create_table_given_multiple_columns_are_inputted_and_first_column_name_is_missing() {
        String statement = "create table user (Integer, username String, password String);";
        Error error = new Error(ErrorCollection.UNSUPPORTED_COLUMN_TYPE);
        assertThrows(RuntimeException.class, () -> syntaxChecker.isValid(statement), error.toString());
    }

    @Test
    void should_throw_exception_1002_when_create_table_given_multiple_columns_are_inputted_and_first_column_definition_is_missing() {
        String statement = "create table user (id, username String, password String);";
        Error error = new Error(ErrorCollection.UNSUPPORTED_COLUMN_TYPE);
        assertThrows(RuntimeException.class, () -> syntaxChecker.isValid(statement), error.toString());
    }

    @Test
    void should_throw_exception_1002_when_create_table_given_multiple_columns_are_inputted_and_multiple_column_name_or_definition_is_missing() {
        String statement = "create table user (id, username String, String);";
        Error error = new Error(ErrorCollection.UNSUPPORTED_COLUMN_TYPE);
        assertThrows(RuntimeException.class, () -> syntaxChecker.isValid(statement), error.toString());
    }

    @Test
    void should_throw_exception_1002_when_create_table_given_column_type_is_not_valid() {
        String statement = "create table user (id Integer, username Str);";
        Error error = new Error(ErrorCollection.UNSUPPORTED_COLUMN_TYPE);
        assertThrows(RuntimeException.class, () -> syntaxChecker.isValid(statement), error.toString());
    }

    @Test
    void should_throw_exception_1003_when_create_table_given_table_name_has_already_exist() {
        String tableName = "user";
        List<ColumnDefinition> columnDefinitions =
                List.of(ColumnDefinition.builder().columnName("id").columnType("Integer").build());
        Database database = DatabaseFactory.getDatabase();
        database.createTable(tableName, columnDefinitions);

        String statement = "create table user (id Integer);";
        Error error = new Error(ErrorCollection.DUPLICATE_TABLE_NAME, tableName);
        assertThrows(RuntimeException.class, () -> syntaxChecker.isValid(statement), error.toString());
    }

    @Test
    void should_throw_exception_1004_when_create_table_given_column_name_has_already_exist() {
        String statement = "create table user (id Integer, username String, username String);";
        Error error = new Error(ErrorCollection.DUPLICATE_COLUMN_NAME, "username");
        assertThrows(RuntimeException.class, () -> syntaxChecker.isValid(statement), error.toString());
    }

    @Test
    void should_throw_exception_1005_when_create_table_given_column_definitions_are_empty() {
        String statement = "create table user ();";
        Error error = new Error(ErrorCollection.EMPTY_COLUMN_DEFINITION);
        assertThrows(RuntimeException.class, () -> syntaxChecker.isValid(statement), error.toString());
    }

    private void assertValidationOfStatement(String statement, boolean expectedResult) {
        boolean result = syntaxChecker.isValid(statement);
        assertEquals(expectedResult, result);
    }

}