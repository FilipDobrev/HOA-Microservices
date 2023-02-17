package nl.tudelft.sem.group23a.activity.domain.activities;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.tudelft.sem.group23a.activity.domain.HasEvents;
import nl.tudelft.sem.group23a.activity.domain.voting.VotingStrategy;

@Getter
@Setter
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name = "activity")
public abstract class Activity extends HasEvents {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "activity_id", nullable = false)
    private long id;

    @Column(name = "activity_hoaId", nullable = false)
    @Embedded
    private HoaId hoaId;

    @Column(name = "activity_description", nullable = false)
    @Embedded
    private Description description;

    @Column(name = "activity_timestamp")
    @Temporal(TemporalType.DATE)
    private Calendar timestamp;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @Column(name = "activity_votes", nullable = false)
    private Set<Vote> votes;

    /**
     * Base constructor for activities.
     *
     * @param hoaId the hoa id
     * @param description the activity description
     * @param timestampShift the number of days in which the activity happens since the time of activity creation
     */
    protected Activity(HoaId hoaId, Description description, int timestampShift) {
        this.hoaId = hoaId;
        this.description = description;
        Calendar timestamp = new GregorianCalendar();
        timestamp.add(Calendar.DAY_OF_MONTH, timestampShift);
        this.timestamp = timestamp;
        this.votes = new HashSet<>();
    }

    /**
     * Base constructor for activities.
     *
     * @param id activity id
     * @param hoaId the hoa id
     * @param description the activity description
     * @param timestampShift the number of days in which the activity happens since the time of activity creation
     */
    protected Activity(long id, HoaId hoaId, Description description, int timestampShift) {
        this.id = id;
        this.hoaId = hoaId;
        this.description = description;
        Calendar timestamp = new GregorianCalendar();
        timestamp.add(Calendar.DAY_OF_MONTH, timestampShift);
        this.timestamp = timestamp;
        this.votes = new HashSet<>();
    }

    /**
     * Returns possible activity choices.
     *
     * @return the possible choices
     */
    public abstract String[] getChoices();

    /**
     * Returns the voting strategy for the activity.
     *
     * @return the voting strategy
     */
    public abstract VotingStrategy getVotingStrategy();

    /**
     * Returns the result of the activity voting.
     *
     * @return the result
     */
    public List<String> getVotingResult() {
        return getVotingStrategy().getResult(this.votes);
    }

    /**
     * This is the voting method.
     *
     * @param choice the choice of the vote
     * @param username the username of the voter
     */
    public void vote(String choice, String username) throws IllegalArgumentException {
        String[] choices = getChoices();
        for (int i = 0; i < choices.length; i++) {
            if (choices[i].equals(choice)) {
                break;
            }
            if (i == choices.length - 1) {
                throw new IllegalArgumentException("This choice is not available");
            }
        }
        // This part removes the vote if someone has already voted and wants to change
        this.votes
                .stream()
                .filter(x -> x.getUsername().equals(username))
                .collect(Collectors.toList())
                .forEach(this.votes::remove);
        // Adds the vote to the list with votes
        this.votes.add(
                Vote.builder()
                        .choice(choice)
                        .username(username)
                        .activity(this)
                        .build()
        );
    }
}
