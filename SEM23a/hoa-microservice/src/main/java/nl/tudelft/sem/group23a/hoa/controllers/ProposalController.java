package nl.tudelft.sem.group23a.hoa.controllers;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.tudelft.sem.group23a.commons.Result;
import nl.tudelft.sem.group23a.hoa.authentication.AuthManager;
import nl.tudelft.sem.group23a.hoa.domain.hoa.HoaBoardService;
import nl.tudelft.sem.group23a.hoa.domain.services.HoaMembershipService;
import nl.tudelft.sem.group23a.hoa.domain.services.HoaService;
import nl.tudelft.sem.group23a.hoa.integrations.activitiesandvoting.ProposalService;
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
public class ProposalController {

    private final transient HoaMembershipService hoaMembershipService;
    private final transient ProposalService proposalService;
    private final transient HoaBoardService hoaBoardService;
    private final transient AuthManager authManager;

    /**
     * A user endpoint for proposal voting.
     * Only board members can use it.
     *
     * @param hoaId the id of the hoa
     * @param requestModel model with user's decision
     * @return http status code base on the result
     */
    @PutMapping("{hoaId}/proposals/{proposalId}")
    public ResponseEntity<?> voteProposal(@Valid @PathVariable Long hoaId,
                                          @Valid @PathVariable Long proposalId,
                                          @Valid @RequestBody VotingRequestModel requestModel) {
        String username = this.authManager.getId();

        if (!this.hoaMembershipService.isBoardMember(hoaId, username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Integer boardMembersCount = this.hoaBoardService.getBoard(hoaId).size();

        Result voteResult = this.proposalService
                .voteForProposal(hoaId, proposalId, username, requestModel.getChoice(), boardMembersCount);

        if (!voteResult.isSuccess()) {
            return ResponseEntity.badRequest().body(voteResult.getErrors());
        }

        return ResponseEntity.noContent().build();
    }
}
