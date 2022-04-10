import java.util.Arrays;
import java.util.List;

public class SyntaxChecker {

    private static final String OPEN_PARENTHESIS = "(";
    private static final String CLOSE_PARENTHESIS = ")";
    private static final String COLUMN_PARAMS_SPLIT = " ";
    private static final int MINIMAL_LENGTH_OF_COLUMN_PARAMS = 2;

    public boolean isValid(String statement) {
        List<String> columnParams = extractColumnParamsFromCreateTableStatement(statement);
        return columnParams.size() >= MINIMAL_LENGTH_OF_COLUMN_PARAMS;
    }

    private List<String> extractColumnParamsFromCreateTableStatement(String statement) {
        int indexOfOpenParenthesis = statement.indexOf(OPEN_PARENTHESIS);
        int indexOfCloseParenthesis = statement.indexOf(CLOSE_PARENTHESIS);
        String contentWithinParenthesis = statement.substring(indexOfOpenParenthesis + 1, indexOfCloseParenthesis);
        return Arrays.asList(contentWithinParenthesis.split(COLUMN_PARAMS_SPLIT));
    }
}
