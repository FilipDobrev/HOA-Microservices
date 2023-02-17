package nl.tudelft.sem.group23a.authentication.domain.services;

import java.util.Optional;
import nl.tudelft.sem.group23a.authentication.domain.repositories.UserRepository;
import nl.tudelft.sem.group23a.authentication.domain.user.Email;
import nl.tudelft.sem.group23a.authentication.domain.user.HoaUser;
import org.springframework.stereotype.Service;

@Service
public class InformationService {
    private final transient UserRepository userRepository;

    public InformationService(UserRepository repository) {
        this.userRepository = repository;
    }

    /**
     * Get the email of the user by id.
     *
     * @param id the id of the user
     * @return Email object with the email of the user
     */
    public Email getEmailById(long id) {
        Optional<HoaUser> user = userRepository.findById(id);
        Email result;
        if (user.isPresent()) {
            result = user.get().getEmail();
        } else {
            result = new Email("");
        }
        return result;
    }
}
