package nl.tudelft.sem.group23a.authentication.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Optional;
import nl.tudelft.sem.group23a.authentication.domain.exceptions.UsernameIsAlreadyTakenException;
import nl.tudelft.sem.group23a.authentication.domain.repositories.UserRepository;
import nl.tudelft.sem.group23a.authentication.domain.services.PasswordEncodingService;
import nl.tudelft.sem.group23a.authentication.domain.services.RegistrationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles({"test", "mockPasswordEncoder"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RegistrationServiceTests {

    @Autowired
    private transient RegistrationService registrationService;

    @Autowired
    private transient PasswordEncodingService mockPasswordEncoder;

    @Autowired
    private transient UserRepository userRepository;

    @Test
    public void createUserValid() throws Exception {
        final Username testName = new Username("TestBot");
        final Password testPassword = new Password("WeakPassword");
        final EncodedPassword testEncoded = new EncodedPassword("EncodedPassword");
        final Email email = new Email("unturned20@abv.bg");

        when(mockPasswordEncoder.hash(testPassword)).thenReturn(testEncoded);

        registrationService.registerUser(testName, testPassword, email);

        HoaUser registered = userRepository.findByUsername(testName).orElseThrow();

        assertThat(registered.getUsername().equals(testName));
        assertThat(registered.getPassword().equals(testEncoded));
        assertThat(registered.getEmail().equals(email));
    }

    @Test
    public void createUserAlreadyExisting() throws Exception {
        final Username testName = new Username("TestBot");
        final Password testPassword = new Password("WeakPassword");
        final Password testPasswordExisting = new Password("WeakerPassword");
        final EncodedPassword testEncoded = new EncodedPassword("EncodedPassword");
        final Email email = new Email("unturned20@abv.bg");

        when(mockPasswordEncoder.hash(testPassword)).thenReturn(testEncoded);

        registrationService.registerUser(testName, testPassword, email);

        assertThatThrownBy(() -> registrationService.registerUser(testName, testPasswordExisting, email))
                .isInstanceOf(UsernameIsAlreadyTakenException.class)
                .hasMessage("TestBot is already taken!");

        HoaUser registered = userRepository.findByUsername(testName).orElseThrow();

        assertThat(registered.getUsername().equals(testName));
        assertThat(registered.getPassword().equals(testEncoded));
        assertThat(registered.getEmail().equals(email));
    }

}
