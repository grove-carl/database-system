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
        Error error = new Error(ErrorCollection.UNSUPPORTED_COLUMN_TYPE);
        assertThrows(Error.class, () -> syntaxChecker.check(statement), error.toString());
    }

    @Test
    void should_throw_exception_1002_when_create_table_given_column_type_is_missing() {
        String statement = "create table user (id);";
        Error error = new Error(ErrorCollection.UNSUPPORTED_COLUMN_TYPE);
        assertThrows(Error.class, () -> syntaxChecker.check(statement), error.toString());
    }

    @Test
    void should_throw_exception_1001_when_create_table_given_table_name_is_missing() {
        String statement = "create table (id integer);";
        Error error = new Error(ErrorCollection.MISSING_TABLE_NAME);
        assertThrows(Error.class, () -> syntaxChecker.check(statement), error.toString());
    }

    @Test
    void should_return_true_when_create_table_given_multiple_columns_are_inputted() {
        String statement = "create table user (id integer, username string, password string);";
        assertDoesNotThrow(() -> syntaxChecker.check(statement));
    }

    @Test
    void should_throw_exception_1002_when_create_table_given_multiple_columns_are_inputted_and_first_column_name_is_missing() {
        String statement = "create table user (integer, username string, password string);";
        Error error = new Error(ErrorCollection.UNSUPPORTED_COLUMN_TYPE);
        assertThrows(Error.class, () -> syntaxChecker.check(statement), error.toString());
    }

    @Test
    void should_throw_exception_1002_when_create_table_given_multiple_columns_are_inputted_and_first_column_definition_is_missing() {
        String statement = "create table user (id, username string, password string);";
        Error error = new Error(ErrorCollection.UNSUPPORTED_COLUMN_TYPE);
        assertThrows(Error.class, () -> syntaxChecker.check(statement), error.toString());
    }

    @Test
    void should_throw_exception_1002_when_create_table_given_multiple_columns_are_inputted_and_multiple_column_name_or_definition_is_missing() {
        String statement = "create table user (id, username string, string);";
        Error error = new Error(ErrorCollection.UNSUPPORTED_COLUMN_TYPE);
        assertThrows(Error.class, () -> syntaxChecker.check(statement), error.toString());
    }

    @Test
    void should_throw_exception_1002_when_create_table_given_column_type_is_not_valid() {
        String statement = "create table user (id integer, username str);";
        Error error = new Error(ErrorCollection.UNSUPPORTED_COLUMN_TYPE);
        assertThrows(Error.class, () -> syntaxChecker.check(statement), error.toString());
    }

    @Test
    void should_throw_exception_1003_when_create_table_given_table_name_has_already_exist() {
        String tableName = "user";
        List<ColumnDefinition> columnDefinitions =
                List.of(ColumnDefinition.builder().columnName("id").columnType("integer").build());
        Database database = DatabaseFactory.getDatabase();
        database.createTable(tableName, columnDefinitions);

        String statement = "create table user (id integer);";
        Error error = new Error(ErrorCollection.DUPLICATE_TABLE_NAME, tableName);
        assertThrows(Error.class, () -> syntaxChecker.check(statement), error.toString());
    }

    @Test
    void should_throw_exception_1004_when_create_table_given_column_name_has_already_exist() {
        String statement = "create table user (id integer, username string, username string);";
        Error error = new Error(ErrorCollection.DUPLICATE_COLUMN_NAME, "username");
        assertThrows(Error.class, () -> syntaxChecker.check(statement), error.toString());
    }

    @Test
    void should_throw_exception_1005_when_create_table_given_column_definitions_are_empty() {
        String statement = "create table user ();";
        Error error = new Error(ErrorCollection.EMPTY_COLUMN_DEFINITION);
        assertThrows(Error.class, () -> syntaxChecker.check(statement), error.toString());
    }

    @Test
    void should_not_throw_exception_when_drop_table_given_specified_table_exists() {
        String tableName = "user";
        List<ColumnDefinition> columnDefinitions =
                List.of(ColumnDefinition.builder().columnName("id").columnType("integer").build());
        Database database = DatabaseFactory.getDatabase();
        database.createTable(tableName, columnDefinitions);

        String statement = "drop table user;";
        assertDoesNotThrow(() -> syntaxChecker.check(statement));
    }

    @Test
    void should_throw_exception_1006_when_drop_table_given_specified_table_not_exists() {
        String statement = "drop table user;";
        String tableName = "user";
        Error error = new Error(ErrorCollection.TABLE_NOT_EXIST, tableName);
        assertThrows(Error.class, () -> syntaxChecker.check(statement), error.toString());
    }

    @Test
    void should_not_throw_exception_when_alter_table_given_statement_syntax_is_correct() {
        String tableName = "user";
        List<ColumnDefinition> columnDefinitions =
                List.of(ColumnDefinition.builder().columnName("id").columnType("integer").build());
        Database database = DatabaseFactory.getDatabase();
        database.createTable(tableName, columnDefinitions);

        String statement = "alter table user add column username string";

        assertDoesNotThrow(() -> syntaxChecker.check(statement));
    }

    @Test
    void should_throw_exception_when_alter_table_given_alter_type_is_add_column_and_table_name_is_missing() {
        String tableName = "user";
        List<ColumnDefinition> columnDefinitions =
                List.of(ColumnDefinition.builder().columnName("id").columnType("integer").build());
        Database database = DatabaseFactory.getDatabase();
        database.createTable(tableName, columnDefinitions);

        String statement = "alter table add column username string";

        Error error = new Error(ErrorCollection.MISSING_TABLE_NAME, tableName);
        assertThrows(Error.class, () -> syntaxChecker.check(statement), error.toString());
    }

    @Test
    void should_throw_exception_1007_when_check_statement_given_command_not_supported() {
        String statement = "modify table";

        Error error = new Error(ErrorCollection.UNSUPPORTED_OPERATION);
        assertThrows(Error.class, () -> syntaxChecker.check(statement), error.toString());
    }

}