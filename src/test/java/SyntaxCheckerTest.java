import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

        boolean result = syntaxChecker.isValid(statement);

        assertTrue(result);
    }

    @Test
    void should_return_false_when_create_table_given_column_name_is_missing() {
        String statement = "create table user (Integer);";

        boolean result = syntaxChecker.isValid(statement);

        assertFalse(result);
    }

    @Test
    void should_return_false_when_create_table_given_column_type_is_missing() {
        String statement = "create table user (id);";

        boolean result = syntaxChecker.isValid(statement);

        assertFalse(result);
    }

    @Test
    void should_return_false_when_create_table_given_table_name_is_missing() {
        String statement = "create table (id Integer);";

        boolean result = syntaxChecker.isValid(statement);

        assertFalse(result);
    }

}