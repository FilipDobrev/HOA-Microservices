package nl.tudelft.sem.group23a.activity.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.GregorianCalendar;
import java.util.List;
import nl.tudelft.sem.group23a.activity.domain.activities.ActivityService;
import nl.tudelft.sem.group23a.activity.domain.activities.Description;
import nl.tudelft.sem.group23a.activity.domain.activities.Gathering;
import nl.tudelft.sem.group23a.activity.domain.activities.HoaId;
import nl.tudelft.sem.group23a.activity.domain.activities.Proposal;
import nl.tudelft.sem.group23a.activity.exceptions.ActivityNotFoundException;
import nl.tudelft.sem.group23a.activity.models.ActivityModel;
import nl.tudelft.sem.group23a.commons.ActivityResponseModel;
import nl.tudelft.sem.group23a.commons.ActivityType;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class ActivityControllerTest {

    private ActivityService activityService;

    private ActivityController activityController;

    Gathering gathering = new Gathering(1, new HoaId(2), new Description("Some description"), 3);

    Proposal proposal = new Proposal(2, new HoaId(2), new Description("Some other description"), 2);

    @BeforeEach
    void setup() {
        activityService = Mockito.mock(ActivityService.class);
        activityController = new ActivityController(activityService);
    }

    @Test
    void getHoaActivitiesTest() {
        // Arrange
        when(activityService.getActivitiesForSpecificHoa(2))
                .thenReturn(List.of(proposal, gathering));

        // Act
        ActivityResponseModel[] response = activityController.getHoaFutureActivities(2L);


        // Assert
        ActivityResponseModel[] expectedModel = new ActivityResponseModel[2];
        expectedModel[0] = new ActivityResponseModel(2L,
                "Some other description",
                this.proposal.getTimestamp(),
                ActivityType.PROPOSAL);
        expectedModel[1] = new ActivityResponseModel(1L,
                "Some description",
                this.gathering.getTimestamp(),
                ActivityType.GATHERING);

        assertThat(response).isEqualTo(expectedModel);
    }

    @Test
    void getActivityUnsuccessful() {
        // Arrange
        when(this.activityService.getActivityById(anyLong())).thenThrow(ActivityNotFoundException.class);

        // Act
        ThrowableAssert.ThrowingCallable result = () -> this.activityController.getActivity(1L);

        // Assert
        assertThatThrownBy(result).isInstanceOf(ActivityNotFoundException.class);
    }

    @Test
    void getActivitySuccessful() {
        // Arrange
        ActivityModel model = new ActivityModel(1, 2, "description", new GregorianCalendar(), null);
        when(this.activityService.getActivityById(anyLong())).thenReturn(model);

        // Act
        ActivityModel result = this.activityController.getActivity(1L);

        // Assert
        assertThat(result).isEqualTo(model);
    }
}
