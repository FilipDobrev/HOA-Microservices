package nl.tudelft.sem.group23a.hoa.integrations.activitiesandvoting;

import lombok.RequiredArgsConstructor;
import nl.tudelft.sem.group23a.commons.Result;
import nl.tudelft.sem.group23a.hoa.integrations.activitiesandvoting.models.ProposalVotingRequestModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Integration service for proposals.
 */
@Service
@RequiredArgsConstructor
public class ProposalService {

    private final transient RestTemplate restTemplate;

    @Value("${urls.activitiesAndVoting}")
    private transient String activitiesAndVotingUrl;

    /**
     * Sends a request to internal endpoint for proposal voting.
     *
     * @param hoaId the id of the hoa
     * @param username who made the vote
     * @param choice the vote
     * @return the operation result
     */
    public Result voteForProposal(long hoaId, long proposalId, String username, String choice, Integer boardMembersCount) {
        String uri = String.format("%s/activities/hoa/%d/proposals/%d/vote", activitiesAndVotingUrl, hoaId, proposalId);
        ProposalVotingRequestModel model = new ProposalVotingRequestModel(choice, username, boardMembersCount);
        HttpEntity<ProposalVotingRequestModel> request = new HttpEntity<>(model);

        ResponseEntity<Void> response = restTemplate.exchange(uri, HttpMethod.PUT, request, Void.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            return Result.unsuccessful(ProposalService.Errors.UNABLE_TO_VOTE_FOR_PROPOSAL);
        }

        return Result.successful();
    }

    protected static class Errors {
        public static final String UNABLE_TO_VOTE_FOR_PROPOSAL = "Unable to vote for proposal";
    }
}
