package nl.tudelft.sem.group23a.hoa.integrations.activitiesandvoting;

import lombok.RequiredArgsConstructor;
import nl.tudelft.sem.group23a.commons.Result;
import nl.tudelft.sem.group23a.hoa.integrations.activitiesandvoting.models.VotingRequestModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Integration service for elections.
 */
@Service
@RequiredArgsConstructor
public class ElectionService {

    private final transient RestTemplate restTemplate;

    @Value("${urls.activitiesAndVoting}")
    private transient String activitiesAndVotingUrl;

    /**
     * Sends a request to internal endpoint for election voting.
     *
     * @param hoaId the id of the hoa
     * @param username who made the vote
     * @param choice the vote
     * @return the operation result
     */
    public Result voteForElection(long hoaId, String username, String choice) {
        String uri = String.format("%s/elections/%d/vote", activitiesAndVotingUrl, hoaId);
        VotingRequestModel model = new VotingRequestModel(choice, username);
        HttpEntity<VotingRequestModel> request = new HttpEntity<>(model);

        ResponseEntity<Void> response = restTemplate.exchange(uri, HttpMethod.PUT, request, Void.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            return Result.unsuccessful(Errors.UNABLE_TO_VOTE_FOR_HOA);
        }

        return Result.successful();
    }

    /**
     * Sends a request to internal endpoint for election application.
     *
     * @param hoaId the id of the hoa
     * @param username who made the vote
     * @param choice the application decision
     * @return the operation result
     */
    public Result applyForElection(long hoaId, String username, String choice) {
        String uri = String.format("%s/elections/%d/application", activitiesAndVotingUrl, hoaId);
        VotingRequestModel model = new VotingRequestModel(choice, username);
        HttpEntity<VotingRequestModel> request = new HttpEntity<>(model);
        ResponseEntity<Void> response = restTemplate.exchange(uri, HttpMethod.PUT, request, Void.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            return Result.unsuccessful(Errors.UNABLE_TO_APPLY_TO_HOA);
        }

        return Result.successful();
    }

    /**
     * Sends a request to internal endpoint to start the election process.
     *
     * @param hoaId the id of the hoa
     * @return the operation result
     */
    public Result startElectionProcess(long hoaId) {
        String uri = String.format("%s/elections/%d", activitiesAndVotingUrl, hoaId);
        ResponseEntity<Void> response = restTemplate.exchange(uri, HttpMethod.PUT, HttpEntity.EMPTY, Void.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            return Result.unsuccessful(Errors.UNABLE_TO_START_ELECTION);
        }

        return Result.successful();
    }

    protected static class Errors {
        public static final String UNABLE_TO_VOTE_FOR_HOA = "Unable to vote for hoa elections";
        public static final String UNABLE_TO_APPLY_TO_HOA = "Unable to apply for hoa elections";
        public static final String UNABLE_TO_START_ELECTION = "Unable to start elections procedure for hoa";
    }
}
