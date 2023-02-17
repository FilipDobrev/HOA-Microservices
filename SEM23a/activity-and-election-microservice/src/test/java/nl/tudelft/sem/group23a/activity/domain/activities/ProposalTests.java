package nl.tudelft.sem.group23a.activity.domain.activities;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

public class ProposalTests {

    @Test
    void getChoicesTest() {
        Proposal proposal = new Proposal(new HoaId(2), new Description("Description"), 2);
        String[] choices = new String[] { "Yes", "No", "Withhold" };
        assertThat(Arrays.equals(choices, proposal.getChoices())).isTrue();
    }
}
