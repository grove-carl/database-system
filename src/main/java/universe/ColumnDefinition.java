package universe;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ColumnDefinition {
    private String columnName;
    private String columnType;
}
