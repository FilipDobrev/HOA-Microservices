package nl.tudelft.sem.group23a.activity.services;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import nl.tudelft.sem.group23a.activity.domain.activities.Activity;
import nl.tudelft.sem.group23a.activity.domain.activities.ActivityRepository;
import nl.tudelft.sem.group23a.activity.domain.activities.Proposal;
import nl.tudelft.sem.group23a.activity.domain.voting.ProposalVotingStrategy;
import nl.tudelft.sem.group23a.activity.models.NotificationRequestModel;
import nl.tudelft.sem.group23a.activity.models.PeopleInHoaResponseModel;
import nl.tudelft.sem.group23a.commons.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ProposalProcedureService {

    private static final String SUBJECT = "Proposal accepted";

    private static final Logger logger = LoggerFactory.getLogger(ProposalProcedureService.class);

    private final transient ActivityRepository activityRepository;
    private final transient RestTemplate restTemplate;

    @Value("{urls.notificationsUrl}")
    private transient String notificationsUrl;

    @Value("{urls.hoaUrl}")
    private transient String hoaUrl;

    /**
     * Updates a proposal vote.
     *
     * @param proposalId the id of the proposal
     * @param choice the proposal choice
     * @param username the user who voted
     * @return whether the operation was successful
     */
    public Result voteForProposal(long proposalId, String choice, String username, int boardCount) {
        Optional<Activity> activityResult = activityRepository.findById(proposalId);

        if (activityResult.isEmpty() || !(activityResult.get() instanceof Proposal)) {
            return Result.unsuccessful(Errors.PROPOSAL_NOT_FOUND);
        }

        Proposal proposal = (Proposal) activityResult.get();
        proposal.vote(choice, username);

        // sets minimum votes to ceil(board count / 2)
        int minimumVotes = boardCount / 2 + boardCount % 2;
        int currentVotes = proposal.getVotes().size();
        if (currentVotes >= minimumVotes) {
            proposal.receivedEnoughVotes();
        }

        activityRepository.save(proposal);

        return Result.successful();
    }

    /**
     * Handles the end of proposal by sending notifications to the user
     * if the proposal is accepted.
     *
     * @param activityId the id of the activity
     */
    public void handleEndOfProposal(Long activityId) {
        Optional<Activity> current = activityRepository.findById(activityId);
        if (!checkValidityOfActivity(current)) {
            return;
        }
        Proposal proposal = (Proposal) current.get();
        String result = proposal.getVotingStrategy().getResult(proposal.getVotes()).get(0);
        if (result.equals(ProposalVotingStrategy.NO)) {
            logger.info(String.format("The proposal %d has not been accepted", activityId));
            return;
        }
        handleAcceptedProposal(proposal);
    }

    /**
     * Checks if the Activity is valid.
     *      If the Activity is not present in the database then this is reported to the logger.
     *      If the Activity is not an instance of Proposal this is also reported to the logger.
     *
     * @param current the activity to be checked
     * @return true if the activity satisfies the above-mentioned criteria
     */
    private boolean checkValidityOfActivity(Optional<Activity> current) {
        if (current.isEmpty()) {
            logger.error("The activity is not present in the database");
            return false;
        } else if (!(current.get() instanceof Proposal)) {
            logger.error("The activity is not instance of proposal");
            return false;
        }
        return true;
    }

    /**
     * Handles the case when the proposal is accepted.
     *
     * @param proposal the accepted proposal
     */
    private void handleAcceptedProposal(Proposal proposal) {
        String[] usernames = getIdsFromHoa(proposal);
        if (usernames.length == 0) {
            return;
        }
        NotificationRequestModel request =
                new NotificationRequestModel(usernames, SUBJECT, proposal.getDescription().getDescriptionValue());
        String notificationsUri = String.format("%s/notifications/send", notificationsUrl);
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<Void> response = restTemplate
                .exchange(notificationsUri,
                        HttpMethod.POST,
                        new HttpEntity<>(request, headers),
                        Void.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            logger.error("The notifications were not send successfully");
            return;
        }
        logger.info(String.format("Notifications sent successfully for proposal %d", proposal.getHoaId().getHoaIdValue()));
    }

    /**
     * Communicates with the HOA microservice to get the ids of
     * the people who are a part of a certain hoa.
     *
     * @param activity the activity for which the ids of the people should be get
     * @return the ids of the people who are a part of the hoa
     */
    public String[] getIdsFromHoa(Activity activity) {
        String hoaUri = String.format("%s/hoa/members/%d", hoaUrl, activity.getHoaId().getHoaIdValue());
        ResponseEntity<PeopleInHoaResponseModel> peopleResponse = restTemplate
                .exchange(hoaUri, HttpMethod.GET, null, PeopleInHoaResponseModel.class);
        if (!peopleResponse.getStatusCode().is2xxSuccessful()) {
            logger.error("The message to the hoa microservice encountered an error");
            return new String[0];
        }
        PeopleInHoaResponseModel people = peopleResponse.getBody();
        if (people == null) {
            return new String[0];
        }
        return people.getIds()
                .stream()
                .map(String::valueOf)
                .toArray(String[]::new);
    }

    protected static class Errors {

        public static final String PROPOSAL_NOT_FOUND = "Unable to find proposal";
    }
}
