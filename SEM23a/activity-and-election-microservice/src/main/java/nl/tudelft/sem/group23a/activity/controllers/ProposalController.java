package nl.tudelft.sem.group23a.activity.controllers;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.tudelft.sem.group23a.activity.models.ProposalVotingRequestModel;
import nl.tudelft.sem.group23a.activity.services.ProposalProcedureService;
import nl.tudelft.sem.group23a.commons.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("activities")
@RequiredArgsConstructor
public class ProposalController {

    private final transient ProposalProcedureService proposalService;

    /**
     * Internal endpoint to submit a user proposal vote.
     *
     * @param hoaId the id of the hoa
     * @param requestModel proposal voting data
     * @return http status code depending on the result
     */
    @PutMapping("hoa/{hoaId}/proposals/{proposalId}/vote")
    public ResponseEntity<?> submitProposalVote(@Valid @PathVariable long hoaId,
                                   @Valid @PathVariable long proposalId,
                                   @Valid @RequestBody ProposalVotingRequestModel requestModel) {

        Result result = this.proposalService.voteForProposal(
                proposalId,
                requestModel.getChoice(),
                requestModel.getUsername(),
                requestModel.getBoardMembersCount());

        if (!result.isSuccess()) {
            return ResponseEntity.badRequest().body(result.getErrors());
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
