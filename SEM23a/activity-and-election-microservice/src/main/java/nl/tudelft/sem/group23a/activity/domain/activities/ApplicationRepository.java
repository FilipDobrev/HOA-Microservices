package nl.tudelft.sem.group23a.activity.domain.activities;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Application activities.
 */
@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    /**
     * Gets the latest Application for a specific hoa.
     *
     * @param hoaId the hoa id
     * @return the last Application
     */
    Optional<Application> findFirstByHoaIdOrderByIdDesc(HoaId hoaId);
}
