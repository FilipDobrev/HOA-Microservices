package nl.tudelft.sem.group23a.activity.domain.activities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import nl.tudelft.sem.group23a.activity.services.ElectionProcedureService;
import org.junit.jupiter.api.Test;

public class ElectionTests {

    @Test
    void getChoicesTest() {
        Set<String> choices = new HashSet<>();
        choices.add("Tim");
        choices.add("Monika");
        Election election = new Election(new HoaId(2), new Description("Description"), choices);
        String[] choose = Arrays.copyOf(
                choices.toArray(),
                choices.size(),
                String[].class);
        assertThat(Arrays.equals(choose, election.getChoices())).isTrue();
    }

    @Test
    public void getVotingStrategy() {
        Set<String> choices = new HashSet<>();
        choices.add("Tim");
        choices.add("Monika");
        Election election = new Election(new HoaId(2), new Description("Description"), choices);
        assertNotNull(election.getVotingStrategy());
    }

    @Test
    public void getCorrectTime() {
        int res = (int) Math.ceil((double) ElectionProcedureService.ELECTION_LENGTH_IN_MINUTES / 1440);
        assertEquals(Election.calculateTimestampShift(), res);
    }
}

