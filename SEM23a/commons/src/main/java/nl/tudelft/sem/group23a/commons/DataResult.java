package nl.tudelft.sem.group23a.commons;

/**
 * Generic result class for operations with data.
 *
 * @param <T> the type of the data.
 */
public class DataResult<T> extends Result {

    /**
     * The inner result data.
     * Has value only when the result is successful.
     */
    private final T data;

    private DataResult(Boolean succeeded, T data, String[] errors) {
        super(succeeded, errors);

        this.data = data;
    }

    /**
     * Returns the data of a successful operation.
     *
     * @return the data.
     * @throws IllegalStateException when method was called for unsuccessful operation.
     */
    public T getData() {
        if (isSuccess()) {
            return data;
        }

        throw new IllegalStateException("getData() is not available with a failed result. Use getErrors() instead.");
    }

    /**
     * Creates a successful result with data and without errors.
     *
     * @param <T> the type of the data
     * @param data the data
     * @return the successful result with data.
     */
    public static <T> DataResult<T> successWith(T data) {
        return new DataResult<T>(true, data, new String[0]);
    }

    /**
     * Creates a failure result with specified errors.
     *
     * @param <T> the type of the data
     * @param errors the errors of the operation.
     * @return the unsuccessful result.
     */
    public static <T> DataResult<T> failureWith(String... errors) {
        return new DataResult<T>(false, null, errors);
    }
}
