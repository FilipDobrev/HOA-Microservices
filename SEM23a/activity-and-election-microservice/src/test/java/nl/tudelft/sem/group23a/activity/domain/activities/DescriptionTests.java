package nl.tudelft.sem.group23a.activity.domain.activities;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;

public class DescriptionTests {

    // boundary test
    @Test
    void testEmptyDescription() {
        ThrowableAssert.ThrowingCallable action = () -> new Description("");
        assertThatThrownBy(action)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid description");
    }

    @Test
    public void correctDescription() {
        String expected = "This is an example description.";
        new Description(expected);
    }

    // boundary test
    @Test
    public void incorrectNotEmptyDescription() {
        String expected = "1This is an invalid description.";
        ThrowableAssert.ThrowingCallable action = () -> new Description(expected);
        assertThatThrownBy(action)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid description");
    }
}
