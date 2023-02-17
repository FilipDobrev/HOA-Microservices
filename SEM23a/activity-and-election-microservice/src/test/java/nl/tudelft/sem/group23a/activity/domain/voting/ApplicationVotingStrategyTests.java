package nl.tudelft.sem.group23a.activity.domain.voting;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import nl.tudelft.sem.group23a.activity.domain.activities.Vote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ApplicationVotingStrategyTests {

    private ApplicationVotingStrategy strategy;

    @BeforeEach
    void setup() {
        this.strategy = new ApplicationVotingStrategy();
    }

    @Test
    void testNoVotes() {
        assertThat(strategy.getResult(new HashSet<>()))
                .isEqualTo(List.of());
    }

    @Test
    void noPositiveAnswers() {
        Vote v1 = new Vote(1, "Dimitar", "No", null);
        Vote v2 = new Vote(2, "Olek", "No", null);
        Vote v3 = new Vote(3, "Filip", "No", null);
        Vote v4 = new Vote(4, "George", "No", null);
        Vote v5 = new Vote(5, "Atour", "No", null);

        Set<Vote> set = new HashSet<>();
        set.add(v1);
        set.add(v2);
        set.add(v3);
        set.add(v4);
        set.add(v5);

        assertThat(strategy.getResult(set))
                .isEqualTo(List.of());
    }

    @Test
    void onePositiveAnswer() {
        Vote v1 = new Vote(1, "Dimitar", "Yes", null);
        Vote v2 = new Vote(2, "Olek", "No", null);
        Vote v3 = new Vote(3, "Filip", "No", null);
        Vote v4 = new Vote(4, "George", "No", null);
        Vote v5 = new Vote(5, "Atour", "No", null);

        Set<Vote> set = new HashSet<>();
        set.add(v1);
        set.add(v2);
        set.add(v3);
        set.add(v4);
        set.add(v5);

        assertThat(strategy.getResult(set))
                .isEqualTo(List.of("Dimitar"));
    }

    @Test
    void multiplePositiveAnswers() {
        Vote v1 = new Vote(1, "Dimitar", "Yes", null);
        Vote v2 = new Vote(2, "Olek", "No", null);
        Vote v3 = new Vote(3, "Filip", "Yes", null);
        Vote v4 = new Vote(4, "George", "No", null);
        Vote v5 = new Vote(5, "Atour", "No", null);

        Set<Vote> set = new HashSet<>();
        set.add(v1);
        set.add(v2);
        set.add(v3);
        set.add(v4);
        set.add(v5);

        assertThat(strategy.getResult(set))
                .containsExactlyInAnyOrder("Dimitar", "Filip");
    }
}
