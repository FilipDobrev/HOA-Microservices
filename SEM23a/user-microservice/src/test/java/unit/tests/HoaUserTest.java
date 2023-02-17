package unit.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import nl.tudelft.sem.group23a.authentication.domain.user.Email;
import nl.tudelft.sem.group23a.authentication.domain.user.EncodedPassword;
import nl.tudelft.sem.group23a.authentication.domain.user.HoaUser;
import nl.tudelft.sem.group23a.authentication.domain.user.Username;
import org.junit.jupiter.api.Test;

class HoaUserTest {
    HoaUser user;

    @Test
    void testConstructor() {
        user = new HoaUser(new Username("Filip"),
                new EncodedPassword("pass"),
                new Email("qweqw@abv.bg"));
        assertEquals("Filip", user.getUsername().toString());
        assertEquals("pass", user.getPassword().toString());
    }

}