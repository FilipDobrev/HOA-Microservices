package nl.tudelft.sem.group23a.activity.domain.activities;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import nl.tudelft.sem.group23a.activity.exceptions.ActivityNotFoundException;
import nl.tudelft.sem.group23a.activity.models.ActivityModel;
import nl.tudelft.sem.group23a.commons.ActivityResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ActivityServiceTest {

    ActivityRepository repo;
    ActivityService service;

    @BeforeEach
    void setup() {
        repo = mock(ActivityRepository.class);
        service = new ActivityService(repo);
    }

    @Test
    void getActivityByIdException() {
        when(repo.findById(any(Long.class))).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getActivityById(1L))
                .isInstanceOf(ActivityNotFoundException.class);
    }

    @Test
    void getActivityById() {
        Activity activity = new Gathering(
                new HoaId(1L),
                new Description("fun"),
                2);
        Optional<Activity> result = Optional.of(activity);
        when(repo.findById(1L))
                .thenReturn(result);

        ActivityModel res = new ActivityModel(activity.getId(),
                activity.getHoaId().getHoaIdValue(),
                activity.getDescription().getDescriptionValue(),
                activity.getTimestamp(),
                activity.getChoices());

        assertEquals(service.getActivityById(1L), res);
    }
}