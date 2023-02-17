package nl.tudelft.sem.group23a.activity.domain.voting;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;
import nl.tudelft.sem.group23a.activity.domain.activities.Vote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MostVotesVotingStrategyTests {

    private MostVotesVotingStrategy strategy;

    @BeforeEach
    void setup() {
        this.strategy = new MostVotesVotingStrategy();
    }

    @Test
    void noVotes() {
        assertThat(strategy.getResult(Set.of()))
                .isEqualTo(List.of());
    }

    @Test
    void oneVote() {
        Vote v1 = new Vote(1, "Dimitar", "Olek", null);
        Set<Vote> set = Set.of(v1);
        assertThat(strategy.getResult(set))
                .containsExactly("Olek");
    }

    @Test
    void multipleVotesLessThanTheBoardMemberNumber() {
        Vote v1 = new Vote(1, "Dimitar", "Olek", null);
        Vote v2 = new Vote(2, "Olek", "Dimitar", null);
        Vote v3 = new Vote(3, "Filip", "Olek", null);
        Vote v4 = new Vote(4, "George", "Olek", null);
        Vote v5 = new Vote(5, "Atour", "Dimitar", null);

        Set<Vote> set = Set.of(v1, v2, v3, v4, v5);

        assertThat(strategy.getResult(set))
                .containsExactly("Olek", "Dimitar");
    }

    @Test
    void multipleVotesMoreThanTheBoardMemberNumber() {
        Vote v1 = new Vote(1, "Dimitar", "Olek", null);
        Vote v2 = new Vote(2, "Olek", "Dimitar", null);
        Vote v3 = new Vote(3, "Filip", "Olek", null);
        Vote v4 = new Vote(4, "George", "Olek", null);
        Vote v5 = new Vote(5, "Atour", "Dimitar", null);
        Vote v6 = new Vote(1, "Dimitar1", "Dimitar", null);
        Vote v7 = new Vote(2, "Olek1", "Filip", null);
        Vote v8 = new Vote(3, "Filip1", "Filip", null);
        Vote v9 = new Vote(4, "George1", "Filip", null);
        Vote v10 = new Vote(5, "Atour1", "George", null);
        Vote v11 = new Vote(1, "Dimitar2", "George", null);
        Vote v12 = new Vote(2, "Olek2", "George", null);
        Vote v13 = new Vote(3, "Filip2", "Atour", null);
        Vote v14 = new Vote(4, "George2", "Atour", null);
        Vote v15 = new Vote(5, "Atour2", "Atour", null);
        Vote v16 = new Vote(5, "Atour1", "George1", null);
        Vote v17 = new Vote(1, "Dimitar2", "George1", null);
        Vote v18 = new Vote(2, "Olek2", "George", null);
        Vote v19 = new Vote(3, "Filip2", "Atour", null);

        Set<Vote> set = Set.of(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10,
                v11, v12, v13, v14, v15, v16, v17, v18, v19);

        assertThat(strategy.getResult(set))
                .containsExactlyInAnyOrder("Olek", "Dimitar", "Atour", "George", "Filip");
    }
}
