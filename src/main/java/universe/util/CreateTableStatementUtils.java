package universe.util;

import universe.ColumnDefinition;
import java.util.ArrayList;
import java.util.List;
import universe.exception.Error;
import universe.exception.ErrorCollection;

public class CreateTableStatementUtils {

    private static final String OPEN_PARENTHESIS = "(";
    private static final String CLOSE_PARENTHESIS = ")";
    private static final String COLUMN_PARAMS_SPLIT = " ";
    private static final String COLUMN_DEFINITION_SPLIT = ",";
    private static final String CREATE_TABLE_KEYWORD = "create table";
    private static final String DROP_TABLE_KEYWORD = "drop table";
    private static final String ALTER_TABLE_KEYWORD = "alter table";
    private static final String ADD_COLUMN_KEYWORD = "add column";
    private static final int WORD_COUNT_OF_COLUMN_DEFINITION = 2;

    public static String extractTableNameFromCreateTableStatement(String statement) {
        int indexOfTailOfCreateTableStatement = CREATE_TABLE_KEYWORD.length();
        int indexOfHeadOfColumnParam = statement.indexOf(OPEN_PARENTHESIS);
        return statement.substring(indexOfTailOfCreateTableStatement, indexOfHeadOfColumnParam).trim();
    }

    public static List<ColumnDefinition> extractColumnDefinitionsFromCreateTableStatement(String statement) {
        String[] textualColumnDefinitions = extractTextualColumnDefinitionWithinParenthesis(statement);
        return mapTextualColumnDefinitionsToObjects(textualColumnDefinitions);
    }

    public static String extractTableNameFromDropTableStatement(String statement) {
        int indexOfTailOfDropTableKeyword = DROP_TABLE_KEYWORD.length();
        int indexOfStatementTerminator = statement.indexOf(";");
        return statement.substring(indexOfTailOfDropTableKeyword, indexOfStatementTerminator).trim();
    }

    public static String extractTableNameFromAlterTableStatement(String statement) {
        int indexOfTailOfAlterTableKeyword = ALTER_TABLE_KEYWORD.length();
        int indexOfHeadOfAddColumnKeyword = statement.indexOf(ADD_COLUMN_KEYWORD);

        return statement.substring(indexOfTailOfAlterTableKeyword, indexOfHeadOfAddColumnKeyword).trim();
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
                throw new Error(ErrorCollection.MISSING_COLUMN_DEFINITION_PARAMS);
            }
            columnDefinitions.add(ColumnDefinition.builder()
                    .columnName(singleColumnDefinition[0])
                    .columnType(singleColumnDefinition[1]).build());
        }
        return columnDefinitions;
    }

    private static boolean columnDefinitionIsNotValid(String[] columnDefinition) {
        return columnDefinition.length != WORD_COUNT_OF_COLUMN_DEFINITION;
    }
}
