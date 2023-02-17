package nl.tudelft.sem.group23a.activity.domain.voting;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import nl.tudelft.sem.group23a.activity.domain.activities.Vote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class ProposalVotingStrategyTests {

    private ProposalVotingStrategy strategy;

    @BeforeEach
    void setup() {
        strategy = new ProposalVotingStrategy();
    }

    /**
     * Boundary test when no votes are received.
     */
    @Test
    void noVotesReceived() {
        assertThat(strategy.getResult(Set.of()))
                .containsExactly("No");
    }

    @Test
    void noNegativeVotes() {
        Vote v1 = new Vote(1, "Dimitar", "Yes", null);
        Vote v2 = new Vote(2, "Olek", "Withhold", null);
        Vote v3 = new Vote(3, "Filip", "Yes", null);
        Vote v4 = new Vote(4, "George", "Yes", null);
        Vote v5 = new Vote(5, "Atour", "Yes", null);
        Set<Vote> votes = Set.of(v1, v2, v3, v4, v5);

        assertThat(strategy.getResult(votes))
                .containsExactly("Yes");
    }

    @Test
    void onlyWithholds() {
        Vote v1 = new Vote(1, "Dimitar", "Withhold", null);
        Vote v2 = new Vote(2, "Lia", "Withhold", null);
        Vote v3 = new Vote(3, "Filip", "Withhold", null);
        Set<Vote> votes = Set.of(v1, v2, v3);

        assertThat(strategy.getResult(votes))
                .containsExactly("No");
    }

    @Test
    void equalNegativeAndPositive() {
        Vote v1 = new Vote(1, "Dimitar", "Yes", null);
        Vote v2 = new Vote(2, "Lia", "Yes", null);
        Vote v3 = new Vote(3, "Filip", "No", null);
        Vote v4 = new Vote(4, "George", "No", null);
        Set<Vote> votes = Set.of(v1, v2, v3, v4);
        assertThat(strategy.getResult(votes))
                .containsExactly("No");
    }

    @Test
    void positiveOutcome() {
        Vote v1 = new Vote(1, "Dimitar", "Yes", null);
        Vote v2 = new Vote(2, "Lia", "Yes", null);
        Vote v3 = new Vote(3, "Filip", "Yes", null);
        Vote v4 = new Vote(4, "George", "No", null);
        Set<Vote> votes = Set.of(v1, v2, v3, v4);
        assertThat(strategy.getResult(votes))
                .containsExactly("Yes");

    }
}
