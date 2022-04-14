import java.util.List;
import lombok.Data;

@Data
public class Table {

    private String name;
    private List<ColumnDefinition> definitions;

}
