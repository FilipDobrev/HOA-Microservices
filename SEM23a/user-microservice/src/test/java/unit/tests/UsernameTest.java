package unit.tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import nl.tudelft.sem.group23a.authentication.domain.user.Username;
import org.junit.jupiter.api.Test;

class UsernameTest {

    @Test
    void testToString() {
        Username name = new Username("Name");
        assertEquals(name.toString(), "Name");
    }

    @Test
    void testInvalidNameNull() {
        assertThatThrownBy(() -> new Username(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid username pick a new one!");
    }

    @Test
    void testInvalidNameEmpty() {
        assertThatThrownBy(() -> new Username(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid username pick a new one!");
    }

    //boundary test
    @Test
    void testValidNameLong() {
        Username name = new Username("q".repeat(127));
        assertThat(name.toString().length() == 127);
    }

    //boundary test
    @Test
    void testInvalidNameLong() {
        assertThatThrownBy(() -> new Username("q".repeat(128)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid username pick a new one!");
    }

    //boundary test
    @Test
    void testInvalidNameTooLong() {
        assertThatThrownBy(() -> new Username("q".repeat(129)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid username pick a new one!");
    }

}