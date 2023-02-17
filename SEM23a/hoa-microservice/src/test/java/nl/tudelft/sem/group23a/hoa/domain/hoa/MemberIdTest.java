package nl.tudelft.sem.group23a.hoa.domain.hoa;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

public class MemberIdTest {

    @Test
    public void testValidationPasses() {
        new MemberId("some-passing-name");
    }

    @Test
    public void testValidationFails() {
        assertThatThrownBy(() -> new MemberId("some not passing-name")).isInstanceOf(IllegalArgumentException.class);
    }
}
