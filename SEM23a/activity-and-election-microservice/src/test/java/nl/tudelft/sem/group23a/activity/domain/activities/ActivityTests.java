package nl.tudelft.sem.group23a.activity.domain.activities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;

public class ActivityTests {

    @Test
    void correctVoteTest() {
        Proposal proposal = new Proposal(new HoaId(2), new Description("Description"), 3);

        proposal.vote("Yes", "username");
        assertThat(proposal.getVotes().size()).isEqualTo(1);
        Vote vote = new ArrayList<>(proposal.getVotes()).get(0);
        assertThat(vote.getUsername()).isEqualTo("username");
        assertThat(vote.getChoice()).isEqualTo("Yes");
    }

    @Test
    void changeVoteTest() {
        Proposal proposal = new Proposal(new HoaId(2), new Description("Description"), 5);

        proposal.vote("No", "username");
        proposal.vote("Yes", "username");

        assertThat(proposal.getVotes().size()).isEqualTo(1);
        Vote tested = (new ArrayList<>(proposal.getVotes())).get(0);
        assertThat(tested.getChoice()).isEqualTo("Yes");
        assertThat(tested.getUsername()).isEqualTo("username");
    }

    @Test
    void wrongVoteTest() {
        Proposal proposal = new Proposal(new HoaId(2), new Description("Description"), 1);
        ThrowableAssert.ThrowingCallable action = () -> proposal.vote("Don't know", "username");
        assertThatThrownBy(action)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("This choice is not available");
    }
}
