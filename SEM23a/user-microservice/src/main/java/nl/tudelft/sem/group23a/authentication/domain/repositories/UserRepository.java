package nl.tudelft.sem.group23a.authentication.domain.repositories;

import java.util.Optional;
import nl.tudelft.sem.group23a.authentication.domain.user.HoaUser;
import nl.tudelft.sem.group23a.authentication.domain.user.Username;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<HoaUser, String> {

    /**
     * Finds the user in the database by a given username.
     *
     * @param username the name to search by
     * @return Returns an optional depending if the user exists or not
     */

    Optional<HoaUser> findByUsername(Username username);

    /**
     * Checks if a user with that username exists.
     *
     * @param username the name to check
     * @return true if the user exists within the database
     */
    boolean existsByUsername(Username username);

    /**
     * The email of the user.
     *
     * @param id the id of the user
     * @return an Email object containing the email
     */
    Optional<HoaUser> findById(Long id);
}
