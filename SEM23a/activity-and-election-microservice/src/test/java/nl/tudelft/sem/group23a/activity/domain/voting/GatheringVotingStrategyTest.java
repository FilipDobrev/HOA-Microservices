package nl.tudelft.sem.group23a.activity.domain.voting;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import nl.tudelft.sem.group23a.activity.domain.activities.Vote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GatheringVotingStrategyTest {

    GatheringVotingStrategy strategy;

    @BeforeEach
    void setup() {
        strategy = new GatheringVotingStrategy();
    }

    @Test
    void testNoVotes() {
        assertThat(strategy.getResult(new HashSet<>()))
                .isEqualTo(List.of());
    }


    @Test
    void getResult() {
        Vote v1 = new Vote(1, "Dimitar", "Going", null);
        Vote v2 = new Vote(2, "Olek", "Interested", null);
        Vote v3 = new Vote(3, "Filip", "Not Going", null);
        Vote v4 = new Vote(4, "George", "Not Going", null);
        Vote v5 = new Vote(5, "Atour", "Not Interested", null);

        Set<Vote> set = new HashSet<>();
        set.add(v1);
        set.add(v2);
        set.add(v3);
        set.add(v4);
        set.add(v5);
        assertThat(strategy.getResult(set))
                .isEqualTo(List.of("Going: 1", "Not Interested: 1", "Not Going: 2", "Interested: 1"));
    }
}