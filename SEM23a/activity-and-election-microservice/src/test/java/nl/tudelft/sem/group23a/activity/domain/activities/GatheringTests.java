package nl.tudelft.sem.group23a.activity.domain.activities;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

public class GatheringTests {

    @Test
    void getChoicesTest() {
        Gathering gathering = new Gathering(new HoaId(2), new Description("Description"), 1);
        String[] choices = new String[] { "Interested", "Going", "Not Going", "Not Interested" };
        assertThat(Arrays.equals(choices, gathering.getChoices())).isTrue();
    }
}
