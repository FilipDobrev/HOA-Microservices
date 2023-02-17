package nl.tudelft.sem.group23a.authentication.domain.builder;

import static org.assertj.core.api.Assertions.assertThat;

import nl.tudelft.sem.group23a.authentication.domain.user.HoaUser;
import nl.tudelft.sem.group23a.commons.exceptions.NotAllNecessaryFieldsAddedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDirectorTest {

    HoaUserBuilder builder;
    UserDirector director;

    @BeforeEach
    void setup() {
        builder = new HoaUserBuilder();
        director = new UserDirector(builder);
    }

    @Test
    void basicUser() throws NotAllNecessaryFieldsAddedException {
        HoaUser basic = director.basicUser();
        assertThat("Default".equals(basic.getUsername().toString()));
        assertThat("DefaultEncodedPassword".equals(basic.getPassword().toString()));
    }

    @Test
    void fullUser() throws NotAllNecessaryFieldsAddedException {
        HoaUser full = director.fullUser();
        assertThat("Default".equals(full.getUsername().toString()));
        assertThat("DefaultEncodedPassword".equals(full.getPassword().toString()));
        assertThat("default@mail.nl".equals(full.getEmail().toString()));
    }
}