package nl.tudelft.sem.group23a.activity.domain.activities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

public class ApplicationTests {

    @Test
    void getChoicesTest() {
        Application application = new Application(new HoaId(2), new Description("Description"));
        String[] choices = new String[] { "Yes", "No" };
        assertThat(Arrays.equals(choices, application.getChoices())).isTrue();
    }

    @Test
    public void getVotingStrategy() {
        Application application = new Application(new HoaId(2), new Description("Description"));
        assertNotNull(application.getVotingStrategy());
    }
}
