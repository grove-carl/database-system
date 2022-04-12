import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SyntaxChecker {

    private static final String OPEN_PARENTHESIS = "(";
    private static final String CLOSE_PARENTHESIS = ")";
    private static final String COLUMN_PARAMS_SPLIT = " ";
    private static final String COLUMN_DEFINITION_SPLIT = ",";
    private static final String CREATE_TABLE_KEYWORD = "create table";

    public boolean isValid(String statement) {
        String tableName = extractTableNameFromCreateTableStatement(statement);
        if (tableName.isEmpty()) {
            return false;
        }
        List<ColumnDefinition> columnDefinitions = extractColumnParamsFromCreateTableStatement(statement);
        return !columnDefinitions.isEmpty();
    }

    private String extractTableNameFromCreateTableStatement(String statement) {
        int indexOfTailOfCreateTableStatement = CREATE_TABLE_KEYWORD.length();
        int indexOfHeadOfColumnParam = statement.indexOf(OPEN_PARENTHESIS);
        return statement.substring(indexOfTailOfCreateTableStatement, indexOfHeadOfColumnParam).trim();
    }

    private List<ColumnDefinition> extractColumnParamsFromCreateTableStatement(String statement) {
        int indexOfOpenParenthesis = statement.indexOf(OPEN_PARENTHESIS);
        int indexOfCloseParenthesis = statement.indexOf(CLOSE_PARENTHESIS);
        String contentWithinParenthesis = statement.substring(indexOfOpenParenthesis + 1, indexOfCloseParenthesis);
        String[] originColumnDefinitions = contentWithinParenthesis.trim().split(COLUMN_DEFINITION_SPLIT);

        List<ColumnDefinition> columnDefinitions = new ArrayList<>();
        for (String originColumnDefinition : originColumnDefinitions) {
            String[] singleColumnDefinition = originColumnDefinition.trim().split(COLUMN_PARAMS_SPLIT);
            if (singleColumnDefinition.length != 2) {
                return Collections.emptyList();
            }
            columnDefinitions.add(ColumnDefinition.builder().columnName(singleColumnDefinition[0]).columnType(singleColumnDefinition[1]).build());
        }
        return columnDefinitions;
    }
}
