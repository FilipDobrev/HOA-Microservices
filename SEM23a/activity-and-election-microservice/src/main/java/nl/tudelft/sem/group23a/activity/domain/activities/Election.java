package nl.tudelft.sem.group23a.activity.domain.activities;

import java.util.Arrays;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.group23a.activity.domain.activities.events.ElectionWasCreatedEvent;
import nl.tudelft.sem.group23a.activity.domain.voting.MostVotesVotingStrategy;
import nl.tudelft.sem.group23a.activity.domain.voting.VotingStrategy;
import nl.tudelft.sem.group23a.activity.services.ElectionProcedureService;

@NoArgsConstructor
@Entity
public class Election extends Activity {

    @ElementCollection
    @CollectionTable(name = "election_choices", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "choices", nullable = false)
    private Set<String> choices;

    /**
     * Constructor for Election activity.
     *
     * @param hoaId the hoa id
     * @param description the activity description
     * @param choices the election choices
     */
    public Election(HoaId hoaId, Description description, Set<String> choices) {
        super(hoaId, description, calculateTimestampShift());
        this.choices = choices;
        long hoaIdLong = getHoaId().getHoaIdValue();
        this.recordThat(new ElectionWasCreatedEvent(hoaIdLong));
    }

    /**
     * Constructor for Election activity.
     *
     * @param id the election id
     * @param hoaId the hoa id
     * @param description the activity description
     * @param choices the election choices
     */
    public Election(long id, HoaId hoaId, Description description, Set<String> choices) {
        super(id, hoaId, description, calculateTimestampShift());
        this.choices = choices;
        long hoaIdLong = getHoaId().getHoaIdValue();
        this.recordThat(new ElectionWasCreatedEvent(hoaIdLong));
    }

    @Override
    public String[] getChoices() {
        return Arrays.copyOf(
                this.choices.toArray(),
                this.choices.size(),
                String[].class
        );
    }

    @Override
    public VotingStrategy getVotingStrategy() {
        return new MostVotesVotingStrategy();
    }

    /**
     * Calculates how much the timestamp has to be shifted from now, based on the length of the election process.
     *
     * @return the days to be shifted
     */
    public static int calculateTimestampShift() {
        return (int) Math.ceil((double) ElectionProcedureService.ELECTION_LENGTH_IN_MINUTES / 1440);
    }
}
