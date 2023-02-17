package nl.tudelft.sem.group23a.hoa.integrations.activitiesandvoting.models;

import lombok.Data;

/**
 * Data transfer model for proposal voting.
 */
@Data
public class ProposalVotingRequestModel {
    private final String choice;
    private final String username;
    private final Integer boardMembersCount;
}
