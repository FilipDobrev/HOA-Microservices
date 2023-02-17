package nl.tudelft.sem.group23a.activity.domain.activities;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import nl.tudelft.sem.group23a.activity.exceptions.ActivityNotFoundException;
import nl.tudelft.sem.group23a.activity.models.ActivityCreationRequestModel;
import nl.tudelft.sem.group23a.activity.models.ActivityModel;
import org.springframework.stereotype.Service;

/**
 * Service for activities.
 */
@Service
@RequiredArgsConstructor
public class ActivityService {

    private final transient ActivityRepository activityRepository;

    /**
     * Returns the activities associated with a specific hoa.
     *
     * @param hoaId the hoa id
     * @return the activities
     */
    public List<Activity> getActivitiesForSpecificHoa(long hoaId) {
        return activityRepository.getActivitiesByHoaIdOrderByIdDesc(new HoaId(hoaId));
    }

    /**
     * Returns a single specific activity.
     *
     * @param activityId the activity id
     * @return the activity
     */
    public ActivityModel getActivityById(long activityId) {
        Optional<Activity> activityResult = activityRepository.findById(activityId);
        if (activityResult.isEmpty()) {
            throw new ActivityNotFoundException();
        }

        Activity activity = activityResult.get();

        return new ActivityModel(activity.getId(),
              activity.getHoaId().getHoaIdValue(),
              activity.getDescription().getDescriptionValue(),
              activity.getTimestamp(),
              activity.getChoices());
    }

    /**
     * Creates a new Activity.
     *
     * @param details of the new Activity
     * @return id of the new Activity
     */
    public long createActivity(ActivityCreationRequestModel details) {
        Description description = new Description(details.getDescription());
        HoaId hoaId = new HoaId(details.getHoaId());
        int days = details.getDays();

        Activity activity;

        switch (details.getType()) {
            case GATHERING:
                activity = new Gathering(hoaId, description, days);
                break;
            case PROPOSAL:
                activity = new Proposal(hoaId, description, days);
                break;
            default:
                throw new IllegalArgumentException("This type of activity does not exist! Cannot create activity "
                        + "with description " + description + " for HOA " + hoaId + " at " + days);
        }

        return this.activityRepository.save(activity).getId();
    }
}
