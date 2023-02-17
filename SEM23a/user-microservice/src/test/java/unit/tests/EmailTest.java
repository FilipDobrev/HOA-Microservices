package unit.tests;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import nl.tudelft.sem.group23a.authentication.domain.user.Email;
import org.junit.jupiter.api.Test;

class EmailTest {


    @Test
    void testToStringValidMail() {
        Email email = new Email("neshto@abv.bg");
        assertTrue(email.toString().equals("neshto@abv.bg"));
    }

    @Test
    void testInvalidMailEmpty() {
        Email mail = new Email("");
        assertThat(mail.toString().equals(""));
    }

    @Test
    void testInvalidMailNull() {
        Email mail = new Email(null);
        assertNull(mail.toString());
    }

    @Test
    void testInvalidMailMissingFront() {
        Email email;
        assertThatThrownBy(() -> new Email("@abv.bg"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Please enter a valid mail!");
    }

    @Test
    void testInvalidMailMissingBack() {
        Email email;
        assertThatThrownBy(() -> new Email("asdasd@abvbg"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Please enter a valid mail!");
    }

    @Test
    void testInvalidMailMissingAt() {
        Email email;
        assertThatThrownBy(() -> new Email("asdasdabv.bg"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Please enter a valid mail!");
    }

    //boundary test
    @Test
    void tooLongEmail() {
        Email email;
        assertThatThrownBy(() -> new Email("a".repeat(252) + "@a.g"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Please enter a valid mail!");
    }


    //boundary test
    @Test
    void veryLongEmail() {
        Email email;
        assertThatThrownBy(() -> new Email("a".repeat(253) + "@a.g"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Please enter a valid mail!");
    }

    //boundary test
    @Test
    void maxSizeEmail() {
        Email email = new Email("a".repeat(251) + "@a.g");
        assertThat(email.toString().length() == 255);
    }
}