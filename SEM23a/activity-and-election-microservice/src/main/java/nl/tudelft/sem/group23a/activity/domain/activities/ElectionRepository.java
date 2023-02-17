package nl.tudelft.sem.group23a.activity.domain.activities;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Election activities.
 */
@Repository
public interface ElectionRepository extends JpaRepository<Election, Long> {

    /**
     * Gets the latest Election for a specific hoa.
     *
     * @param hoaId the hoa id
     * @return the last Election
     */
    Optional<Election> findFirstByHoaIdOrderByIdDesc(HoaId hoaId);

    /**
     * Checks if any election exists wherein the given user is running.
     *
     * @param choice the user for whom to check if they are already running
     * @return a boolean indicating the result of this check.
     */
    boolean existsByChoicesContaining(String choice);
}
