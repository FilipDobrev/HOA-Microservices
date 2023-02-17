package nl.tudelft.sem.group23a.activity.domain.activities;

import java.util.Arrays;
import javax.persistence.Entity;
import javax.persistence.Transient;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.group23a.activity.domain.activities.events.ApplicationWasCreatedEvent;
import nl.tudelft.sem.group23a.activity.domain.voting.ApplicationVotingStrategy;
import nl.tudelft.sem.group23a.activity.domain.voting.VotingStrategy;
import nl.tudelft.sem.group23a.activity.services.ElectionProcedureService;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
public class Application extends Activity {

    @Transient
    private final String[] choices = new String[] { "Yes", "No" };

    /**
     * Constructor for Application activity.
     *
     * @param hoaId the hoa id
     * @param description the activity description
     */
    public Application(HoaId hoaId, Description description) {
        super(hoaId, description, calculateTimestampShift());
        long hoaIdLong = getHoaId().getHoaIdValue();
        this.recordThat(new ApplicationWasCreatedEvent(hoaIdLong));
    }

    /**
     * Constructor for Application activity.
     *
     * @param id application id
     * @param hoaId the hoa id
     * @param description the activity description
     */
    public Application(long id, HoaId hoaId, Description description) {
        super(id, hoaId, description, calculateTimestampShift());
        long hoaIdLong = getHoaId().getHoaIdValue();
        this.recordThat(new ApplicationWasCreatedEvent(hoaIdLong));
    }

    @Override
    public String[] getChoices() {
        return Arrays.copyOf(choices, choices.length);
    }

    @Override
    public VotingStrategy getVotingStrategy() {
        return new ApplicationVotingStrategy();
    }

    /**
     * Calculates how much the timestamp has to be shifted from now, based on the length of the application process.
     *
     * @return the days to be shifted
     */
    private static int calculateTimestampShift() {
        return (int) Math.ceil((double) ElectionProcedureService.APPLICATION_LENGTH_IN_MINUTES / 1440);
    }
}
