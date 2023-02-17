package nl.tudelft.sem.group23a.commons;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

public class ResultTests {

    @Test
    public void successReturnsCorrectInstance() {
        var result = Result.successful();

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getErrors()).isEmpty();
    }

    @Test
    public void failureReturnsCorrectInstance() {
        var errors = new String[] { "error0", "error1" };
        var result = Result.unsuccessful(errors);

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getErrors()).hasSameElementsAs(Arrays.asList(errors));
    }
}
