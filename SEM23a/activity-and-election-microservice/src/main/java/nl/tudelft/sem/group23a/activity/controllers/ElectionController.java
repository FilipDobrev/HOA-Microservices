package nl.tudelft.sem.group23a.activity.controllers;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.tudelft.sem.group23a.activity.models.UserChoiceRequestModel;
import nl.tudelft.sem.group23a.activity.services.ElectionProcedureService;
import nl.tudelft.sem.group23a.commons.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for elections.
 */
@Controller
@RequestMapping("elections")
@RequiredArgsConstructor
public class ElectionController {

    private final transient ElectionProcedureService electionService;

    /**
     * Internal endpoint to start the election procedures for a specific hoa.
     *
     * @param hoaId the id of the hoa
     * @return http status code depending on the result
     */
    @PutMapping("{hoaId}")
    public ResponseEntity<?> startElectionProcess(@PathVariable long hoaId) {
        if (electionService.hasCurrentlyRunningElection(hoaId)) {
            return ResponseEntity.ok().build();
        }

        electionService.startElectionProcess(hoaId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Internal endpoint to submit a user application for hoa elections.
     *
     * @param hoaId the id of the hoa
     * @param requestModel election choice data
     * @return http status code depending on the result
     */
    @PutMapping("{hoaId}/application")
    public ResponseEntity<String[]> apply(@PathVariable long hoaId,
                                   @Valid @RequestBody UserChoiceRequestModel requestModel) {
        if (!electionService.hasCurrentlyRunningElection(hoaId)) {
            return ResponseEntity.badRequest().build();
        }

        if (electionService.isCurrentlyRunningInElection(requestModel.getUsername())) {
            return ResponseEntity.badRequest().build();
        }

        Result result = electionService.applyForElection(hoaId, requestModel.getChoice(), requestModel.getUsername());
        if (!result.isSuccess()) {
            return ResponseEntity.badRequest().body(result.getErrors());
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Internal endpoint to submit a user vote for hoa elections.
     *
     * @param hoaId the id of the hoa
     * @param requestModel election choice data
     * @return http status code depending on the result
     */
    @PutMapping("{hoaId}/vote")
    public ResponseEntity<String[]> vote(@PathVariable long hoaId,
                                  @Valid @RequestBody UserChoiceRequestModel requestModel) {
        if (!electionService.hasCurrentlyRunningElection(hoaId)) {
            return ResponseEntity.badRequest().build();
        }

        Result result = electionService.voteForElection(hoaId, requestModel.getChoice(), requestModel.getUsername());
        if (!result.isSuccess()) {
            return ResponseEntity.badRequest().body(result.getErrors());
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}

