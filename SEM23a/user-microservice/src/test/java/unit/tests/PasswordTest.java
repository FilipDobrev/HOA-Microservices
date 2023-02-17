package unit.tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import nl.tudelft.sem.group23a.authentication.domain.user.Password;
import org.junit.jupiter.api.Test;

class PasswordTest {

    @Test
    void testToString() {
        Password pass = new Password("pass");
        assertEquals(pass.toString(), "pass");
    }

    @Test
    void testInvalidPassNull() {
        assertThatThrownBy(() -> new Password(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid password, pick a new one!");
    }

    @Test
    void testInvalidPassEmpty() {
        assertThatThrownBy(() -> new Password(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid password, pick a new one!");
    }

    //boundary test
    @Test
    void testInvalidPassLong() {
        assertThatThrownBy(() -> new Password("q".repeat(128)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid password, pick a new one!");
    }

    @Test
    void testInvalidPassTooLong() {
        assertThatThrownBy(() -> new Password("q".repeat(150)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid password, pick a new one!");
    }

    //boundary test
    @Test
    void testJustAboutValid() {
        Password pass = new Password("q".repeat(127));
        assertThat(pass.toString().length() == 127);
    }

    @Test
    public void testLongPassword() {
        StringBuilder longPass = new StringBuilder();
        for (int i = 0; i < 129; i++) {
            longPass.append("a");
        }

        assertThatThrownBy(() -> new Password(longPass.toString()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid password, pick a new one!");
    }

}