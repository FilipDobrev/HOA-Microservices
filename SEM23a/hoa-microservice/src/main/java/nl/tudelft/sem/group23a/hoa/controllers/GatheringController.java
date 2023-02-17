package nl.tudelft.sem.group23a.hoa.controllers;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.tudelft.sem.group23a.commons.Result;
import nl.tudelft.sem.group23a.hoa.authentication.AuthManager;
import nl.tudelft.sem.group23a.hoa.domain.services.HoaMembershipService;
import nl.tudelft.sem.group23a.hoa.integrations.activitiesandvoting.GatheringService;
import nl.tudelft.sem.group23a.hoa.models.VotingRequestModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("hoa")
@RequiredArgsConstructor
public class GatheringController {

    private final transient HoaMembershipService hoaMembershipService;
    private final transient GatheringService gatheringService;
    private final transient AuthManager authManager;

    /**
     * A user endpoint for gathering reacting.
     *
     * @param hoaId the id of the hoa
     * @param requestModel model with user's decision
     * @return http status code base on the result
     */
    @PutMapping("{hoaId}/gatherings/{gatheringId}")
    public ResponseEntity<?> reactGathering(@Valid @PathVariable Long hoaId,
                                          @Valid @PathVariable Long gatheringId,
                                          @Valid @RequestBody VotingRequestModel requestModel) {
        String username = this.authManager.getId();

        if (!this.hoaMembershipService.isMember(hoaId, username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Result reactionResult = this.gatheringService
                .reactToGathering(hoaId, gatheringId, username, requestModel.getChoice());

        if (!reactionResult.isSuccess()) {
            return ResponseEntity.badRequest().body(reactionResult.getErrors());
        }

        return ResponseEntity.noContent().build();
    }
}
