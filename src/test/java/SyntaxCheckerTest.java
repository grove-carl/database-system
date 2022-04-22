import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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
        database.dropAllTables();
    }

    @Test
    void should_not_throw_exception_when_create_table_given_one_column_with_column_name_and_data_type() {
        String statement = "create table user (id integer);";
        assertDoesNotThrow(() -> syntaxChecker.check(statement));
    }

    @Test
    void should_throw_exception_1002_when_create_table_given_column_name_is_missing() {
        String statement = "create table user (integer);";

        assertThrowErrorWhenCheckStatement(ErrorCollection.UNSUPPORTED_OPERATION, statement);
    }

    @Test
    void should_throw_exception_1002_when_create_table_given_column_type_is_missing() {
        String statement = "create table user (id);";

        assertThrowErrorWhenCheckStatement(ErrorCollection.UNSUPPORTED_OPERATION, statement);
    }

    @Test
    void should_throw_exception_1001_when_create_table_given_table_name_is_missing() {
        String statement = "create table (id integer);";

        assertThrowErrorWhenCheckStatement(ErrorCollection.MISSING_TABLE_NAME, statement);
    }

    @Test
    void should_not_throw_exception_when_create_table_given_multiple_columns_are_inputted() {
        String statement = "create table user (id integer, username string, password string);";
        assertDoesNotThrow(() -> syntaxChecker.check(statement));
    }

    @Test
    void should_throw_exception_1002_when_create_table_given_multiple_columns_are_inputted_and_first_column_name_is_missing() {
        String statement = "create table user (integer, username string, password string);";

        assertThrowErrorWhenCheckStatement(ErrorCollection.UNSUPPORTED_OPERATION, statement);
    }

    @Test
    void should_throw_exception_1002_when_create_table_given_multiple_columns_are_inputted_and_first_column_definition_is_missing() {
        String statement = "create table user (id, username string, password string);";

        assertThrowErrorWhenCheckStatement(ErrorCollection.UNSUPPORTED_OPERATION, statement);
    }

    @Test
    void should_throw_exception_1002_when_create_table_given_multiple_columns_are_inputted_and_multiple_column_name_or_definition_is_missing() {
        String statement = "create table user (id, username string, string);";

        assertThrowErrorWhenCheckStatement(ErrorCollection.UNSUPPORTED_OPERATION, statement);
    }

    @Test
    void should_throw_exception_1002_when_create_table_given_column_type_is_not_valid() {
        String statement = "create table user (id integer, username str);";

        assertThrowErrorWhenCheckStatement(ErrorCollection.UNSUPPORTED_OPERATION, statement);
    }

    @Test
    void should_throw_exception_1003_when_create_table_given_table_name_has_already_exist() {
        generateUserTableWithSingleColumn();

        String statement = "create table user (id integer);";

        assertThrowErrorWhenCheckStatement(ErrorCollection.DUPLICATE_TABLE_NAME, statement);
    }

    @Test
    void should_throw_exception_1004_when_create_table_given_column_name_has_already_exist() {
        String statement = "create table user (id integer, username string, username string);";

        assertThrowErrorWhenCheckStatement(ErrorCollection.DUPLICATE_COLUMN_NAME, statement);
    }

    @Test
    void should_throw_exception_1005_when_create_table_given_column_definitions_are_empty() {
        String statement = "create table user ();";

        assertThrowErrorWhenCheckStatement(ErrorCollection.EMPTY_COLUMN_DEFINITION, statement);
    }

    @Test
    void should_not_throw_exception_when_drop_table_given_specified_table_exists() {
        generateUserTableWithSingleColumn();

        String statement = "drop table user;";

        assertDoesNotThrow(() -> syntaxChecker.check(statement));
    }

    @Test
    void should_throw_exception_1006_when_drop_table_given_specified_table_not_exists() {
        String statement = "drop table user;";
        String tableName = "user";

        assertThrowErrorWhenCheckStatement(ErrorCollection.TABLE_NOT_EXIST, statement);
    }

    @Test
    void should_not_throw_exception_when_alter_table_given_statement_syntax_is_correct() {
        generateUserTableWithSingleColumn();

        String statement = "alter table user add column username string";

        assertDoesNotThrow(() -> syntaxChecker.check(statement));
    }

    @Test
    void should_throw_exception_1001_when_alter_table_given_alter_type_is_add_column_and_table_name_is_missing() {
        generateUserTableWithSingleColumn();

        String statement = "alter table add column username string";

        assertThrowErrorWhenCheckStatement(ErrorCollection.MISSING_TABLE_NAME, statement);
    }

    @Test
    void should_throw_exception_1007_when_check_statement_given_command_not_supported() {
        String statement = "modify table";

        assertThrowErrorWhenCheckStatement(ErrorCollection.UNSUPPORTED_OPERATION, statement);
    }

    private void assertThrowErrorWhenCheckStatement(ErrorCollection expectedError, String statement) {
        Error error = new Error(expectedError);
        assertThrows(Error.class, () -> syntaxChecker.check(statement), error.toString());
    }

    private void generateUserTableWithSingleColumn() {
        String tableName = "user";
        List<ColumnDefinition> columnDefinitions =
                List.of(ColumnDefinition.builder().columnName("id").columnType("integer").build());
        Database database = DatabaseFactory.getDatabase();
        database.createTable(tableName, columnDefinitions);
    }

}