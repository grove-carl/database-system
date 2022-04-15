package universe.util;

import universe.ColumnDefinition;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CreateTableStatementUtils {

    private static final String OPEN_PARENTHESIS = "(";
    private static final String CLOSE_PARENTHESIS = ")";
    private static final String COLUMN_PARAMS_SPLIT = " ";
    private static final String COLUMN_DEFINITION_SPLIT = ",";
    private static final String CREATE_TABLE_KEYWORD = "create table";

    public static String extractTableNameFromCreateTableStatement(String statement) {
        int indexOfTailOfCreateTableStatement = CREATE_TABLE_KEYWORD.length();
        int indexOfHeadOfColumnParam = statement.indexOf(OPEN_PARENTHESIS);
        return statement.substring(indexOfTailOfCreateTableStatement, indexOfHeadOfColumnParam).trim();
    }

    public static List<ColumnDefinition> extractColumnDefinitionsFromCreateTableStatement(String statement) {
        String[] textualColumnDefinitions = extractTextualColumnDefinitionWithinParenthesis(statement);
        return mapTextualColumnDefinitionsToObjects(textualColumnDefinitions);
    }

    private static String[] extractTextualColumnDefinitionWithinParenthesis(String statement) {
        int indexOfOpenParenthesis = statement.indexOf(OPEN_PARENTHESIS);
        int indexOfCloseParenthesis = statement.indexOf(CLOSE_PARENTHESIS);
        String contentWithinParenthesis = statement.substring(indexOfOpenParenthesis + 1, indexOfCloseParenthesis);
        return contentWithinParenthesis.trim().split(COLUMN_DEFINITION_SPLIT);
    }

    private static List<ColumnDefinition> mapTextualColumnDefinitionsToObjects(String[] textualColumnDefinitions) {
        List<ColumnDefinition> columnDefinitions = new ArrayList<>();
        for (String originColumnDefinition : textualColumnDefinitions) {
            String[] singleColumnDefinition = originColumnDefinition.trim().split(COLUMN_PARAMS_SPLIT);
            if (columnDefinitionIsNotValid(singleColumnDefinition)) {
                return Collections.emptyList();
            }
            columnDefinitions.add(ColumnDefinition.builder()
                    .columnName(singleColumnDefinition[0])
                    .columnType(singleColumnDefinition[1]).build());
        }
        return columnDefinitions;
    }

    private static boolean columnDefinitionIsNotValid(String[] columnDefinition) {
        return columnDefinition.length != 2;
    }
}
