package universe.exception;

import lombok.Getter;

@Getter
public enum ErrorCollection {

    MISSING_COLUMN_DEFINITION_PARAMS("1000", "missing required column definition param"),
    MISSING_TABLE_NAME("1001", "table name is not provided"),
    UNSUPPORTED_COLUMN_TYPE("1002", "column type is invalid"),
    DUPLICATE_TABLE_NAME("1003", "table `%s` has already exists"),
    DUPLICATE_COLUMN_NAME("1004", "duplicate column `%s`"),
    EMPTY_COLUMN_DEFINITION("1005", "column definition should not be empty");

    private final String code;
    private final String description;

    ErrorCollection(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
