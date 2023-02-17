package nl.tudelft.sem.group23a.activity.services;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import nl.tudelft.sem.group23a.activity.domain.activities.ActivityRepository;
import nl.tudelft.sem.group23a.activity.domain.activities.Description;
import nl.tudelft.sem.group23a.activity.domain.activities.Gathering;
import nl.tudelft.sem.group23a.activity.domain.activities.HoaId;
import nl.tudelft.sem.group23a.activity.domain.activities.Proposal;
import nl.tudelft.sem.group23a.activity.models.GatheringModel;
import nl.tudelft.sem.group23a.commons.DataResult;
import nl.tudelft.sem.group23a.commons.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GatheringServiceTest {

    private ActivityRepository activityRepository;
    private GatheringService gatheringService;

    @BeforeEach
    void setUp() {
        this.activityRepository = mock(ActivityRepository.class);
        this.gatheringService = new GatheringService(this.activityRepository);
    }

    @Test
    void getNonExistentGatheringById() {
        // Arrange
        when(this.activityRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        DataResult<GatheringModel> result = this.gatheringService.getGatheringById(1L);

        // Assert
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getErrors()).contains(GatheringService.Errors.GATHERING_NOT_FOUND);
    }

    @Test
    void getGatheringByIdThatIsNoGathering() {
        // Arrange
        Proposal fauxGathering = new Proposal();
        when(this.activityRepository.findById(anyLong())).thenReturn(Optional.of(fauxGathering));

        // Act
        DataResult<GatheringModel> result = this.gatheringService.getGatheringById(1L);

        // Assert
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getErrors()).contains(GatheringService.Errors.GATHERING_NOT_FOUND);
    }

    @Test
    void getGatheringByIdSuccess() {
        // Arrange
        Gathering gathering = new Gathering(new HoaId(1L), new Description("Description"), 1);
        when(this.activityRepository.findById(anyLong())).thenReturn(Optional.of(gathering));

        // Act
        DataResult<GatheringModel> result = this.gatheringService.getGatheringById(1L);

        // Assert
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getData()).isNotNull();
    }

    @Test
    void reactToNonExistentGathering() {
        // Arrange
        when(this.activityRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Result result = this.gatheringService.reactToGathering(1L, "choice", "username");

        // Assert
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getErrors()).contains(GatheringService.Errors.GATHERING_NOT_FOUND);
    }

    @Test
    void reactToGatheringThatIsNoGathering() {
        // Arrange
        Proposal fauxGathering = new Proposal();
        when(this.activityRepository.findById(anyLong())).thenReturn(Optional.of(fauxGathering));

        // Act
        Result result = this.gatheringService.reactToGathering(1L, "choice", "username");

        // Assert
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getErrors()).contains(GatheringService.Errors.GATHERING_NOT_FOUND);
    }

    @Test
    void reactToGatheringThrows() {
        // Arrange
        Gathering gathering = new Gathering(new HoaId(1L), new Description("Description"), 1);
        when(this.activityRepository.findById(anyLong())).thenReturn(Optional.of(gathering));

        // Act
        ThrowingCallable result = () -> this.gatheringService.reactToGathering(1L, "choice",
                "username");

        // Assert
        assertThatThrownBy(result).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void reactToGatheringSuccessful() {
        // Arrange
        Gathering gathering = new Gathering(new HoaId(1L), new Description("Description"), 1);
        when(this.activityRepository.findById(anyLong())).thenReturn(Optional.of(gathering));

        // Act
        Result result = this.gatheringService.reactToGathering(1L, "Interested", "username");

        // Assert
        assertThat(result.isSuccess()).isTrue();
    }
}