package universe.config;

import java.util.ArrayList;
import java.util.List;

public class ValidColumnTypes {

    public static List<String> get() {
        List<String> validColumnTypes = new ArrayList<>();
        validColumnTypes.add("Integer");
        validColumnTypes.add("String");
        return validColumnTypes;
    }
}
