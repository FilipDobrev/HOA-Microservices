package nl.tudelft.sem.group23a.hoa.controllers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import nl.tudelft.sem.group23a.commons.Result;
import nl.tudelft.sem.group23a.hoa.authentication.AuthManager;
import nl.tudelft.sem.group23a.hoa.domain.services.HoaMembershipService;
import nl.tudelft.sem.group23a.hoa.integrations.activitiesandvoting.ElectionService;
import nl.tudelft.sem.group23a.hoa.models.VotingRequestModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for hoa elections.
 */
@RestController
@RequestMapping("hoa")
@RequiredArgsConstructor
public class ElectionController {

    private final transient HoaMembershipService hoaMembershipService;
    private final transient ElectionService electionService;
    private final transient AuthManager authManager;

    /**
     * A user endpoint for applying for an election.
     *
     * @param hoaId the id of the hoa
     * @param requestModel model with user's decision
     * @return http status code base on the result
     */
    @PutMapping("{hoaId}/election/apply")
    public ResponseEntity<?> applyForBoard(@NonNull @PathVariable Long hoaId,
                                           @NonNull @RequestBody VotingRequestModel requestModel) {
        String username = this.authManager.getId();

        this.hoaMembershipService.confirmIsEligibleForBoard(hoaId, username);

        Result applicationResult = this.electionService.applyForElection(hoaId, username, requestModel.getChoice());

        if (!applicationResult.isSuccess()) {
            return ResponseEntity.badRequest().body(applicationResult.getErrors());
        }

        return ResponseEntity.noContent().build();
    }

    /**
     * A user endpoint for election voting.
     *
     * @param hoaId the id of the hoa
     * @param requestModel model with user's decision
     * @return http status code base on the result
     */
    @PutMapping("{hoaId}/election/vote")
    public ResponseEntity<?> voteForBoard(@NonNull @PathVariable Long hoaId,
                                          @NonNull @RequestBody VotingRequestModel requestModel) {
        String username = this.authManager.getId();

        if (!this.hoaMembershipService.isMember(hoaId, username)
                || username.equals(requestModel.getChoice())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Result voteResult = this.electionService.voteForElection(hoaId, username, requestModel.getChoice());

        if (!voteResult.isSuccess()) {
            return ResponseEntity.badRequest().body(voteResult.getErrors());
        }

        return ResponseEntity.noContent().build();
    }

    /**
     *  A user endpoint for starting a subsequent election for a given hoa.
     *  Only board members can use it.
     *
     * @param hoaId the id of the hoa
     * @return http status code base on the result
     */
    @PutMapping("{hoaId}/election")
    public ResponseEntity<String[]> startNextElection(@NonNull @PathVariable Long hoaId) {
        String username = this.authManager.getId();

        this.hoaMembershipService.confirmIsBoardMember(hoaId, username);

        Result electionStartResult = this.electionService.startElectionProcess(hoaId);

        if (!electionStartResult.isSuccess()) {
            return ResponseEntity.badRequest().body(electionStartResult.getErrors());
        }

        return ResponseEntity.noContent().build();
    }
}
