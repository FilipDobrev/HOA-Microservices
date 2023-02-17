package nl.tudelft.sem.group23a.activity.controllers;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.tudelft.sem.group23a.activity.models.GatheringModel;
import nl.tudelft.sem.group23a.activity.models.UserChoiceRequestModel;
import nl.tudelft.sem.group23a.activity.services.GatheringService;
import nl.tudelft.sem.group23a.commons.DataResult;
import nl.tudelft.sem.group23a.commons.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("activities")
@RequiredArgsConstructor
public class GatheringController {

    private final transient GatheringService gatheringService;

    /**
     * Internal endpoint to submit a user gathering reaction.
     *
     * @param hoaId the id of the hoa
     * @param gatheringId the if of the gathering
     * @param requestModel choice data
     * @return http status code depending on the result
     */
    @PutMapping("hoa/{hoaId}/gatherings/{gatheringId}/vote")
    public ResponseEntity<?> submitGatheringReaction(@Valid @PathVariable long hoaId,
                                                     @Valid @PathVariable long gatheringId,
                                                     @Valid @RequestBody UserChoiceRequestModel requestModel) {

        Result result = this.gatheringService.reactToGathering(
                gatheringId,
                requestModel.getChoice(),
                requestModel.getUsername());

        if (!result.isSuccess()) {
            return ResponseEntity.badRequest().body(result.getErrors());
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * User endpoint to get gathering and its reactions.
     *
     * @param hoaId the id of the hoa
     * @param gatheringId the if of the gathering
     * @return http status code depending on the result
     */
    @GetMapping("hoa/{hoaId}/gatherings/{gatheringId}")
    public ResponseEntity<?> getGathering(@Valid @PathVariable long hoaId, @Valid @PathVariable long gatheringId) {

        DataResult<GatheringModel> result = this.gatheringService.getGatheringById(gatheringId);

        if (!result.isSuccess()) {
            return ResponseEntity.badRequest().body(result.getErrors());
        }

        return ResponseEntity.ok(result.getData());
    }
}