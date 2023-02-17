package nl.tudelft.sem.group23a.hoa.integrations.activitiesandvoting.models;

import lombok.Data;

/**
 * Data transfer model for username and their vote.
 */
@Data
public class VotingRequestModel {
    private final String choice;
    private final String username;
}
