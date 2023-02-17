package nl.tudelft.sem.group23a.activity.domain.activities;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    @Override
    <A extends Activity> A save(A activity);

    <A extends Activity> Optional<A> findById(long id);

    @Override
    void delete(Activity act);

    List<Activity> getActivitiesByHoaIdOrderByIdDesc(HoaId hoaId);
}