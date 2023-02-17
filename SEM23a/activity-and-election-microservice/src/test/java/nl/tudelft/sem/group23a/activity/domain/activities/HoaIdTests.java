package nl.tudelft.sem.group23a.activity.domain.activities;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

public class HoaIdTests {

    // boundary test
    @Test
    void incorrectValidationTest() {
        assertThatThrownBy(() -> new HoaId(-1)).isInstanceOf(IllegalArgumentException.class);
    }

    // boundary test
    @Test
    void correctValidationTestOnBoundary() {
        new HoaId(0);
    }

    // boundary test
    @Test
    void correctValidationTest() {
        new HoaId(1);
    }
}
