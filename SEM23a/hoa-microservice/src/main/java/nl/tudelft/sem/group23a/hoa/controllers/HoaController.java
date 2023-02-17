package nl.tudelft.sem.group23a.hoa.controllers;

import static nl.tudelft.sem.group23a.hoa.infrastructure.HttpServletRequestExtensions.getBearerToken;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.tudelft.sem.group23a.commons.ActivityResponseModel;
import nl.tudelft.sem.group23a.hoa.authentication.AuthManager;
import nl.tudelft.sem.group23a.hoa.domain.services.HoaMembershipService;
import nl.tudelft.sem.group23a.hoa.domain.services.HoaService;
import nl.tudelft.sem.group23a.hoa.integrations.activitiesandvoting.ActivityService;
import nl.tudelft.sem.group23a.hoa.models.HoaCreationModel;
import nl.tudelft.sem.group23a.hoa.models.HoaIdModel;
import nl.tudelft.sem.group23a.hoa.models.HoaJoinModel;
import nl.tudelft.sem.group23a.hoa.models.PeopleInHoaResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("hoa")
public class HoaController {

    private final transient HoaMembershipService hoaMembershipService;
    private final transient HoaService hoaService;
    private final transient ActivityService activityService;
    private final transient AuthManager authManager;
    private final transient HttpServletRequest request;

    /**
     * Creates a new HOA and returns its id.
     *
     * @param creationRequest the HOA's details
     * @return the newly created HOA's id.
     */
    @PostMapping
    public ResponseEntity<HoaIdModel> createHoa(@Valid @RequestBody HoaCreationModel creationRequest) {
        return ResponseEntity.ok(new HoaIdModel(this.hoaService.createHoa(creationRequest)));
    }

    /**
     * Endpoint to join a HOA.
     *
     * @param hoaId the id of the HOA to join
     * @param joinRequest the user's information, pretty much just their address.
     * @return A ResponseEntity indicating whether joining the HOA was successful via status codes.
     */
    @PutMapping("join/{hoaId}")
    public ResponseEntity<?> joinHoa(@Valid @PathVariable Long hoaId,
                                     @Valid @RequestBody HoaJoinModel joinRequest) {
        HoaService.HoaJoinOutcome outcome = this.hoaService.joinHoa(hoaId, joinRequest, authManager.getId());
        if (outcome == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        HttpStatus returnStatus = outcome.status;
        return ResponseEntity.status(returnStatus).build();
    }

    /**
     * Endpoint to leave a HOA.
     *
     * @param hoaId the id of the HOA to leave
     * @return A ResponseEntity indicating whether leaving the HOA was successful via status codes.
     */
    @PutMapping("leave/{hoaId}")
    public ResponseEntity<?> leaveHoa(@Valid @PathVariable Long hoaId) {
        boolean outcome = this.hoaService.leaveHoa(hoaId, authManager.getId());
        if (outcome) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint for the ids of the members in the hoa.
     *
     * @param hoaId the id of the hoa
     * @return a response entity with a list of the people
     */
    @GetMapping("members/{hoaId}")
    public ResponseEntity<PeopleInHoaResponseModel> allMembers(@Valid @PathVariable Long hoaId) {
        return ResponseEntity.ok(new PeopleInHoaResponseModel(this.hoaService.memberIds(hoaId)));
    }

    /**
     * Gets the noticeboard of the given HOA, if it is requested by an eligible user.
     *
     * @param hoaId the hoaID of the hoa whose notice board we're looking for
     * @return an error if the requester is not eligible or the description of the activities!
     */
    @GetMapping("notice-board/{hoaId}")
    public ResponseEntity<?> getNoticeBoard(@Valid @PathVariable Long hoaId) {
        if (!this.hoaMembershipService.allowedToGetNoticeboard(hoaId, this.authManager.getId())) {
            return ResponseEntity.badRequest().body("This noticeboard is not accessible by you!");
        }

        String token = getBearerToken(this.request);
        ActivityResponseModel[] activities = this.activityService.getActivities(hoaId, token, ActivityService.Time.FUTURE);

        return ResponseEntity.ok(activities);
    }

    /**
     * Gets the history of the given HOA, if it is requested by an eligible user.
     *
     * @param hoaId the hoaID of the hoa whose notice board we're looking for
     * @return an error if the requester is not eligible or the description of the activities!
     */
    @GetMapping("history/{hoaId}")
    public ResponseEntity<?> getHoaHistory(@Valid @PathVariable Long hoaId) {
        if (!this.hoaMembershipService.allowedToGetNoticeboard(hoaId, this.authManager.getId())) {
            return ResponseEntity.badRequest().body("This history is not accessible by you!");
        }

        String token = getBearerToken(this.request);
        ActivityResponseModel[] activities = this.activityService.getActivities(hoaId, token, ActivityService.Time.PAST);

        return ResponseEntity.ok(activities);
    }
}
