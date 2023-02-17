package nl.tudelft.sem.group23a.activity.services;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import nl.tudelft.sem.group23a.activity.domain.activities.Activity;
import nl.tudelft.sem.group23a.activity.domain.activities.ActivityRepository;
import nl.tudelft.sem.group23a.activity.domain.activities.Gathering;
import nl.tudelft.sem.group23a.activity.models.GatheringModel;
import nl.tudelft.sem.group23a.commons.DataResult;
import nl.tudelft.sem.group23a.commons.Result;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GatheringService {

    private final transient ActivityRepository activityRepository;

    /**
     * Returns a single specific gathering and its reactions.
     *
     * @param gatheringId the gathering id
     * @return the gathering
     */
    public DataResult<GatheringModel> getGatheringById(long gatheringId) {
        Optional<Activity> activityResult = activityRepository.findById(gatheringId);

        if (activityResult.isEmpty() || !(activityResult.get() instanceof Gathering)) {
            return DataResult.failureWith(GatheringService.Errors.GATHERING_NOT_FOUND);
        }

        Gathering gathering = (Gathering) activityResult.get();

        GatheringModel model = new GatheringModel(gathering.getId(),
                gathering.getHoaId().getHoaIdValue(),
                gathering.getDescription().getDescriptionValue(),
                gathering.getTimestamp(),
                gathering.getVotingResult());

        return DataResult.successWith(model);
    }

    /**
     * Updates a gathering reaction.
     *
     * @param gatheringId the gathering id
     * @return whether the operation was successful
     */
    public Result reactToGathering(long gatheringId, String choice, String username) {
        Optional<Activity> activityResult = activityRepository.findById(gatheringId);

        if (activityResult.isEmpty() || !(activityResult.get() instanceof Gathering)) {
            return Result.unsuccessful(GatheringService.Errors.GATHERING_NOT_FOUND);
        }

        Gathering gathering = (Gathering) activityResult.get();
        gathering.vote(choice, username);
        this.activityRepository.save(gathering);

        return Result.successful();
    }

    protected static class Errors {

        public static final String GATHERING_NOT_FOUND = "Unable to find gathering";
    }
}
