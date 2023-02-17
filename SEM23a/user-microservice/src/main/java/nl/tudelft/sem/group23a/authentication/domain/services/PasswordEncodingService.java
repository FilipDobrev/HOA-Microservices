package nl.tudelft.sem.group23a.authentication.domain.services;

import nl.tudelft.sem.group23a.authentication.domain.user.EncodedPassword;
import nl.tudelft.sem.group23a.authentication.domain.user.Password;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncodingService {
    private final transient PasswordEncoder encoder;

    public PasswordEncodingService(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    /**
     * Encodes the password using spring encoder.
     *
     * @param password the password to be encoded
     * @return the encoded password
     */
    public EncodedPassword hash(Password password) {
        return new EncodedPassword(encoder.encode(password.toString()));
    }
}
