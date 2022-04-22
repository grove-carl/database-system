package universe.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Error extends RuntimeException {

    private final String description;
    private final ErrorType error;

    public Error(ErrorType error) {
        this.error = error;
        this.description = error.getDescription();
    }

    public Error(ErrorType error, Object... params) {
        this.error = error;
        this.description = String.format(error.getDescription(), params);
    }

    @Override
    public String toString() {
        String format = "ERROR (%s): %s";
        return String.format(format, error.getCode(), error.getDescription());
    }
}

