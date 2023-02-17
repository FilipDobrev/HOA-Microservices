package nl.tudelft.sem.group23a.authentication.domain.services;

import nl.tudelft.sem.group23a.authentication.domain.exceptions.UsernameIsAlreadyTakenException;
import nl.tudelft.sem.group23a.authentication.domain.repositories.UserRepository;
import nl.tudelft.sem.group23a.authentication.domain.user.Email;
import nl.tudelft.sem.group23a.authentication.domain.user.EncodedPassword;
import nl.tudelft.sem.group23a.authentication.domain.user.HoaUser;
import nl.tudelft.sem.group23a.authentication.domain.user.Password;
import nl.tudelft.sem.group23a.authentication.domain.user.Username;
import nl.tudelft.sem.group23a.commons.exceptions.NotAllNecessaryFieldsAddedException;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {
    private final transient UserRepository userRepository;
    private final transient PasswordEncodingService pes;

    public RegistrationService(UserRepository repo, PasswordEncodingService es) {
        this.userRepository = repo;
        this.pes = es;
    }

    /**
     * Used to register a user and add the mto the database.
     *
     * @param name  The name of the user
     * @param pass  The password of the user
     * @param email The email of the user if they have provided one
     * @return the new created user.
     * @throws UsernameIsAlreadyTakenException when the username is already taken by a different user
     */
    public HoaUser registerUser(Username name, Password pass, Email email) throws
            UsernameIsAlreadyTakenException, NotAllNecessaryFieldsAddedException {
        if (!userRepository.existsByUsername(name)) {
            System.out.println("Encoding password... ...");
            EncodedPassword encPassword = pes.hash(pass);
            System.out.println("Creating and registering a user");
            HoaUser newUser =
                    HoaUser.builder()
                            .username(name)
                            .password(encPassword)
                            .email(email)
                            .build();

            userRepository.save(newUser);

            return newUser;

        }
        throw new UsernameIsAlreadyTakenException(name);
    }
}
