package nl.tudelft.sem.group23a.activity.domain.activities;

import java.util.Arrays;
import javax.persistence.Entity;
import javax.persistence.Transient;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.group23a.activity.domain.activities.events.ProposalHasFinishedEvent;
import nl.tudelft.sem.group23a.activity.domain.voting.ProposalVotingStrategy;
import nl.tudelft.sem.group23a.activity.domain.voting.VotingStrategy;

@NoArgsConstructor
@Entity
public class Proposal extends Activity {

    @Transient
    private final String[] choices = new String[] { "Yes", "No", "Withhold" };

    /**
     * Constructor for Proposal activity.
     *
     * @param hoaId the hoa id
     * @param description the activity description
     */
    public Proposal(HoaId hoaId, Description description, int daysLater) {
        super(hoaId, description, daysLater);
    }

    /**
     * Constructor for Proposal activity.
     *
     * @param id the proposal id
     * @param hoaId the hoa id
     * @param description the activity description
     */
    public Proposal(long id, HoaId hoaId, Description description, int daysLater) {
        super(id, hoaId, description, daysLater);
    }

    /**
     * Records an event that the proposal has received enough votes.
     */
    public void receivedEnoughVotes() {
        this.recordThat(new ProposalHasFinishedEvent(getId()));
    }

    @Override
    public String[] getChoices() {
        return Arrays.copyOf(choices, choices.length);
    }

    @Override
    public VotingStrategy getVotingStrategy() {
        return new ProposalVotingStrategy();
    }
}
