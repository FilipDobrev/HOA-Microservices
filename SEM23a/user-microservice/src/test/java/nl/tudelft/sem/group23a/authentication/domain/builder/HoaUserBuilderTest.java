package nl.tudelft.sem.group23a.authentication.domain.builder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import nl.tudelft.sem.group23a.authentication.domain.user.Email;
import nl.tudelft.sem.group23a.authentication.domain.user.EncodedPassword;
import nl.tudelft.sem.group23a.authentication.domain.user.HoaUser;
import nl.tudelft.sem.group23a.authentication.domain.user.Username;
import nl.tudelft.sem.group23a.commons.exceptions.NotAllNecessaryFieldsAddedException;
import org.junit.jupiter.api.Test;

class HoaUserBuilderTest {

    final Username testUser = new Username("SomeUser");
    final EncodedPassword testHashedPassword = new EncodedPassword("hashedTestPassword");
    final Email email = new Email("RealMail@true.fx");

    @Test
    void testBuildAllInfo() throws NotAllNecessaryFieldsAddedException {
        HoaUser user = HoaUser.builder()
                .username(testUser)
                .password(testHashedPassword)
                .email(email)
                .build();
        assertEquals("SomeUser", user.getUsername().toString());
        assertEquals("hashedTestPassword", user.getPassword().toString());
        assertEquals("RealMail@true.fx", user.getEmail().toString());
    }

    @Test
    void testBuildNoEmail() throws NotAllNecessaryFieldsAddedException {
        HoaUser user = HoaUser.builder()
                .username(testUser)
                .password(testHashedPassword)
                .build();
        assertEquals("SomeUser", user.getUsername().toString());
        assertEquals("hashedTestPassword", user.getPassword().toString());
        assertEquals(null, user.getEmail());
    }

    @Test
    void throwExceptionNoUsername() throws NotAllNecessaryFieldsAddedException {
        assertThatThrownBy(() -> HoaUser.builder()
                .password(testHashedPassword)
                .email(email)
                .build())
                .isInstanceOf(NotAllNecessaryFieldsAddedException.class)
                .hasMessage("Username was not added to the builder, and build() cannot commence!");
    }

    @Test
    void throwExceptionNoPassword() throws NotAllNecessaryFieldsAddedException {
        assertThatThrownBy(() -> HoaUser.builder()
                .username(testUser)
                .email(email)
                .build())
                .isInstanceOf(NotAllNecessaryFieldsAddedException.class)
                .hasMessage("Encoded password was not added to the builder, and build() cannot commence!");
    }
}