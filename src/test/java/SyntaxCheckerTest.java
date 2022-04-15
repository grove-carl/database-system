import static org.junit.jupiter.api.Assertions.assertEquals;

import universe.SyntaxChecker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SyntaxCheckerTest {

    private SyntaxChecker syntaxChecker;

    @BeforeEach
    void setUp() {
        syntaxChecker = new SyntaxChecker();
    }

    @Test
    void should_return_true_when_create_table_given_one_column_with_column_name_and_data_type() {
        String statement = "create table user (id Integer);";
        boolean expectedResult = true;
        assertValidationOfStatement(statement, expectedResult);
    }

    @Test
    void should_return_false_when_create_table_given_column_name_is_missing() {
        String statement = "create table user (Integer);";
        boolean expectedResult = false;
        assertValidationOfStatement(statement, expectedResult);
    }

    @Test
    void should_return_false_when_create_table_given_column_type_is_missing() {
        String statement = "create table user (id);";
        boolean expectedResult = false;
        assertValidationOfStatement(statement, expectedResult);
    }

    @Test
    void should_return_false_when_create_table_given_table_name_is_missing() {
        String statement = "create table (id Integer);";
        boolean expectedResult = false;
        assertValidationOfStatement(statement, expectedResult);
    }

    @Test
    void should_return_true_when_create_table_given_multiple_columns_are_inputted() {
        String statement = "create table user (id Integer, username String, password String);";
        boolean expectedResult = true;
        assertValidationOfStatement(statement, expectedResult);
    }

    @Test
    void should_return_false_when_create_table_given_multiple_columns_are_inputted_and_first_column_name_is_missing() {
        String statement = "create table user (Integer, username String, password String);";
        boolean expectedResult = false;
        assertValidationOfStatement(statement, expectedResult);
    }

    @Test
    void should_return_false_when_create_table_given_multiple_columns_are_inputted_and_first_column_definition_is_missing() {
        String statement = "create table user (id, username String, password String);";
        boolean expectedResult = false;
        assertValidationOfStatement(statement, expectedResult);
    }

    @Test
    void should_return_false_when_create_table_given_multiple_columns_are_inputted_and_multiple_column_name_or_definition_is_missing() {
        String statement = "create table user (id, username String, String);";
        boolean expectedResult = false;
        assertValidationOfStatement(statement, expectedResult);
    }

    private void assertValidationOfStatement(String statement, boolean expectedResult) {
        boolean result = syntaxChecker.isValid(statement);
        assertEquals(expectedResult, result);
    }

}