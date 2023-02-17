package nl.tudelft.sem.group23a.hoa.domain.hoa;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

public class HoaNameTest {

    @Test
    public void testValidationPasses() {
        new HoaName("some passing-name");
    }

    @Test
    public void testValidationFails() {
        assertThatThrownBy(() -> new MemberId("some nõn passing-name")).isInstanceOf(IllegalArgumentException.class);
    }
}
