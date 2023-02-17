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
 * Integration service for gatherings.
 */
@Service
@RequiredArgsConstructor
public class GatheringService {

    private final transient RestTemplate restTemplate;

    @Value("${urls.activitiesAndVoting}")
    private transient String activitiesAndVotingUrl;

    /**
     * Sends a request to internal endpoint for gathering reaction.
     *
     * @param hoaId the id of the hoa
     * @param username who reacted
     * @param choice the reaction
     * @return the operation result
     */
    public Result reactToGathering(long hoaId, long gatheringId, String username, String choice) {
        String uri = String.format("%s/activities/hoa/%d/gatherings/%d/vote", activitiesAndVotingUrl, hoaId, gatheringId);
        VotingRequestModel model = new VotingRequestModel(choice, username);
        HttpEntity<VotingRequestModel> request = new HttpEntity<>(model);

        ResponseEntity<Void> response = restTemplate.exchange(uri, HttpMethod.PUT, request, Void.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            return Result.unsuccessful(GatheringService.Errors.UNABLE_TO_REACT_TO_GATHERING);
        }

        return Result.successful();
    }

    protected static class Errors {
        public static final String UNABLE_TO_REACT_TO_GATHERING = "Unable to react to gathering";
    }
}
