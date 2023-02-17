package nl.tudelft.sem.group23a.commons;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

public class DataResultTests {

    @Test
    public void successReturnsCorrectInstance() {
        var data = 1;
        var result = DataResult.successWith(data);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getData()).isEqualTo(data);
        assertThat(result.getErrors()).isEmpty();
    }

    @Test
    public void failureReturnsCorrectInstance() {
        var errors = new String[] { "error0", "error1" };
        var result = DataResult.failureWith(errors);

        assertThat(result.isSuccess()).isFalse();
        assertThatThrownBy(result::getData).isInstanceOf(IllegalStateException.class);
        assertThat(result.getErrors()).hasSameElementsAs(Arrays.asList(errors));
    }
}
