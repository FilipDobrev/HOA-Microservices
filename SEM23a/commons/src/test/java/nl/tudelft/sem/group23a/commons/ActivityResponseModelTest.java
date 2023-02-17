package nl.tudelft.sem.group23a.commons;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Calendar;
import java.util.GregorianCalendar;
import org.junit.jupiter.api.Test;

class ActivityResponseModelTest {

    @Test
    void testConstructor() {
        Calendar calendar = new GregorianCalendar();
        ActivityResponseModel tested = new ActivityResponseModel(1L, "Something", calendar, ActivityType.GATHERING);
        assertThat(tested.getId()).isEqualTo(1L);
        assertThat(tested.getDescription()).isEqualTo("Something");
        assertThat(tested.getTimestamp()).isEqualTo(calendar);
        assertThat(tested.getType()).isEqualTo(ActivityType.GATHERING);
    }

    @Test
    void testEmptyConstructor() {
        ActivityResponseModel tested = new ActivityResponseModel();
        assertThat(tested.getId()).isNull();
        assertThat(tested.getDescription()).isNull();
        assertThat(tested.getTimestamp()).isNull();
        assertThat(tested.getType()).isNull();
    }

    @Test
    void testEmptyConstructorFilled() {
        ActivityResponseModel tested = new ActivityResponseModel();
        tested.setDescription("Something");
        tested.setId(1L);
        tested.setTimestamp(new GregorianCalendar(2022, Calendar.DECEMBER, 21));
        tested.setType(ActivityType.ELECTION);
        assertThat(tested.getId()).isEqualTo(1L);
        assertThat(tested.getDescription()).isEqualTo("Something");
        assertThat(tested.getTimestamp()).isEqualTo(new GregorianCalendar(2022, Calendar.DECEMBER, 21));
        assertThat(tested.getType()).isEqualTo(ActivityType.ELECTION);
    }

    @Test
    void testToString() {
        Calendar calendar = new GregorianCalendar(2022, Calendar.DECEMBER, 21, 16, 25);
        ActivityResponseModel tested = new ActivityResponseModel(1L, "Something", calendar, ActivityType.GATHERING);
        assertEquals("ActivityResponseModel(id=1, description=Something, timestamp=21/11/2022/16, type=GATHERING)",
                tested.toString());
    }
}