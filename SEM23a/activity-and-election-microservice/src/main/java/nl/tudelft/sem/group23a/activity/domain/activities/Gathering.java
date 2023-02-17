package nl.tudelft.sem.group23a.activity.domain.activities;

import java.util.Arrays;
import javax.persistence.Entity;
import javax.persistence.Transient;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.group23a.activity.domain.voting.GatheringVotingStrategy;
import nl.tudelft.sem.group23a.activity.domain.voting.VotingStrategy;

@NoArgsConstructor
@Entity
public class Gathering extends Activity {

    @Transient
    private final String[] choices = new String[] { "Interested", "Going", "Not Going", "Not Interested" };

    /**
     * Constructor for Gathering activity.
     *
     * @param hoaId the hoa id
     * @param description the activity description
     */
    public Gathering(HoaId hoaId, Description description, int daysLater) {
        super(hoaId, description, daysLater);
    }

    /**
     * Constructor for Gathering activity.
     *
     * @param id the gathering id
     * @param hoaId the hoa id
     * @param description the activity description
     */
    public Gathering(long id, HoaId hoaId, Description description, int daysLater) {
        super(id, hoaId, description, daysLater);
    }

    @Override
    public String[] getChoices() {
        return Arrays.copyOf(choices, choices.length);
    }

    @Override
    public VotingStrategy getVotingStrategy() {
        return new GatheringVotingStrategy();
    }
}
