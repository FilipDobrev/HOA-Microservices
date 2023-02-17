package nl.tudelft.sem.group23a.activity.controllers;

import java.util.GregorianCalendar;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import nl.tudelft.sem.group23a.activity.domain.activities.Activity;
import nl.tudelft.sem.group23a.activity.domain.activities.ActivityService;
import nl.tudelft.sem.group23a.activity.models.ActivityCreationRequestModel;
import nl.tudelft.sem.group23a.activity.models.ActivityCreationResponseModel;
import nl.tudelft.sem.group23a.activity.models.ActivityModel;
import nl.tudelft.sem.group23a.commons.ActivityResponseModel;
import nl.tudelft.sem.group23a.commons.ActivityType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for activities.
 */
@RestController
@RequestMapping("activities")
@RequiredArgsConstructor
public class ActivityController {

    private final transient ActivityService activityService;

    /**
     * Creates a new Activity.
     *
     * @param creationRequest details of Activity
     * @return id of the new Activity
     */
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ActivityCreationResponseModel createActivity(
          @NonNull @RequestBody ActivityCreationRequestModel creationRequest) {

        long id = this.activityService.createActivity(creationRequest);
        return new ActivityCreationResponseModel(id);
    }

    /**
     * Retrieve a single activity with its possible choices.
     *
     * @param activityId the id of the activity
     * @return response to the http request
     */
    @GetMapping("{activityId}")
    @ResponseStatus(code = HttpStatus.OK)
    public ActivityModel getActivity(@PathVariable long activityId) {
        return this.activityService.getActivityById(activityId);
    }

    /**
     * Retrieve the future activities for a specific hoa.
     *
     * @param hoaId ID of the hoa
     * @return response to the http request
     */
    @GetMapping("future/{hoaId}")
    @ResponseStatus(code = HttpStatus.OK)
    public ActivityResponseModel[] getHoaFutureActivities(@PathVariable Long hoaId) {
        // check if user is part of hoaId should be done in HOA microservice;
        return this.activityService.getActivitiesForSpecificHoa(hoaId)
              .stream()
              .filter(activity -> activity.getTimestamp().after(new GregorianCalendar()))
              .map(x -> new ActivityResponseModel(x.getId(),
                    x.getDescription().getDescriptionValue(),
                    x.getTimestamp(),
                    getType(x)))
              .toArray(ActivityResponseModel[]::new);
    }

    /**
     * Retrieve the past activities for a specific hoa.
     *
     * @param hoaId ID of the hoa
     * @return response to the http request
     */
    @GetMapping("past/{hoaId}")
    @ResponseStatus(code = HttpStatus.OK)
    public ActivityResponseModel[] getHoaPastActivities(@PathVariable Long hoaId) {
        // check if user is part of hoaId should be done in HOA microservice;
        return this.activityService.getActivitiesForSpecificHoa(hoaId)
              .stream()
              .filter(activity -> activity.getTimestamp().before(new GregorianCalendar()))
              .map(x -> new ActivityResponseModel(x.getId(),
                    x.getDescription().getDescriptionValue(),
                    x.getTimestamp(),
                    getType(x)))
              .toArray(ActivityResponseModel[]::new);
    }

    /**
     * Get the type of the activity depending on the instance.
     *
     * @param activity the activity
     * @return the type of the activity
     */
    private ActivityType getType(Activity activity) {
        ActivityType type;
        switch (activity.getClass().getSimpleName()) {
            case "Application":
                type = ActivityType.APPLICATION;
                break;
            case "Election":
                type = ActivityType.ELECTION;
                break;
            case "Gathering":
                type = ActivityType.GATHERING;
                break;
            case "Proposal":
                type = ActivityType.PROPOSAL;
                break;
            default:
                throw new IllegalArgumentException("No such type");
        }
        return type;
    }
}
