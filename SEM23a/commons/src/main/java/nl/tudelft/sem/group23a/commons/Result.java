package nl.tudelft.sem.group23a.commons;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Result class for operations.
 */
@Getter
public class Result {

    private final boolean success;
    private final String[] errors;

    /**
     * Internal result constructor.
     *
     * @param success whether the operation succeeded
     * @param errors the errors of the operation
     */
    protected Result(Boolean success, String[] errors) {
        this.success = success;

        if (!success && errors.length == 0) {
            this.errors = new String[] { "Unsuccessful operation." };
        } else {
            this.errors = errors;
        }
    }

    /**
     * Creates a successful result without errors.
     *
     * @return the successful result.
     */
    public static Result successful() {
        return new Result(true, new String[0]);
    }

    /**
     * Creates a failure result with specified errors.
     *
     * @param errors the errors of the operation.
     * @return the unsuccessful result.
     */
    public static Result unsuccessful(String... errors) {
        return new Result(false, errors);
    }
}
