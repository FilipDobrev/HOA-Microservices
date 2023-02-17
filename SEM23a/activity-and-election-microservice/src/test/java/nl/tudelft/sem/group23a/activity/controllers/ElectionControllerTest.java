package nl.tudelft.sem.group23a.activity.controllers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import nl.tudelft.sem.group23a.activity.models.UserChoiceRequestModel;
import nl.tudelft.sem.group23a.activity.services.ElectionProcedureService;
import nl.tudelft.sem.group23a.commons.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class ElectionControllerTest {

    private ElectionController electionController;
    private ElectionProcedureService electionService;

    @BeforeEach
    void setUp() {
        this.electionService = mock(ElectionProcedureService.class);
        this.electionController = new ElectionController(this.electionService);
    }

    @Test
    void applyWhenAlreadyRunningTest() {
        when(this.electionService.isCurrentlyRunningInElection(anyString())).thenReturn(true);
        when(this.electionService.hasCurrentlyRunningElection(any(Long.class))).thenReturn(true);

        UserChoiceRequestModel model = new UserChoiceRequestModel("me", "me_once_more");

        ResponseEntity<String[]> response = this.electionController.apply(1L, model);

        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
    }

    @Test
    void startElectionProcess() {
        // Arrange
        when(this.electionService.hasCurrentlyRunningElection(1L)).thenReturn(true);
        when(this.electionService.hasCurrentlyRunningElection(2L)).thenReturn(false);

        // Act
        ResponseEntity<?> result1 = this.electionController.startElectionProcess(1L);
        ResponseEntity<?> result2 = this.electionController.startElectionProcess(2L);

        // Assert
        assertThat(result1.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result2.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void applyNoElection() {
        // Arrange
        UserChoiceRequestModel requestModel = new UserChoiceRequestModel("1", "username");
        when(this.electionService.hasCurrentlyRunningElection(anyLong())).thenReturn(false);

        // Act
        ResponseEntity<String[]> result = this.electionController.apply(1L, requestModel);

        // Assert
        assertThat(result.getStatusCode().is4xxClientError()).isTrue();
    }

    @Test
    void applyAlreadyRunningInElection() {
        // Arrange
        UserChoiceRequestModel requestModel = new UserChoiceRequestModel("1", "username");
        when(this.electionService.hasCurrentlyRunningElection(anyLong())).thenReturn(true);
        when(this.electionService.isCurrentlyRunningInElection(anyString())).thenReturn(true);

        // Act
        ResponseEntity<String[]> result = this.electionController.apply(1L, requestModel);

        // Assert
        assertThat(result.getStatusCode().is4xxClientError()).isTrue();
    }

    @Test
    void applyUnsuccessful() {
        // Arrange
        UserChoiceRequestModel requestModel = new UserChoiceRequestModel("1", "username");
        when(this.electionService.hasCurrentlyRunningElection(anyLong())).thenReturn(true);
        when(this.electionService.isCurrentlyRunningInElection(anyString())).thenReturn(false);
        when(this.electionService.applyForElection(anyLong(), anyString(), anyString()))
                .thenReturn(Result.unsuccessful());

        // Act
        ResponseEntity<?> result = this.electionController.apply(1L, requestModel);

        // Assert
        assertThat(result.getStatusCode().is4xxClientError()).isTrue();
    }

    @Test
    void applySuccessful() {
        // Arrange
        UserChoiceRequestModel requestModel = new UserChoiceRequestModel("1", "username");
        when(this.electionService.hasCurrentlyRunningElection(anyLong())).thenReturn(true);
        when(this.electionService.isCurrentlyRunningInElection(anyString())).thenReturn(false);
        when(this.electionService.applyForElection(anyLong(), anyString(), anyString()))
                .thenReturn(Result.successful());

        // Act
        ResponseEntity<?> result = this.electionController.apply(1L, requestModel);

        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void voteNoElection() {
        // Arrange
        UserChoiceRequestModel requestModel = new UserChoiceRequestModel("1", "username");
        when(this.electionService.hasCurrentlyRunningElection(anyLong())).thenReturn(false);

        // Act
        ResponseEntity<String[]> result = this.electionController.vote(1L, requestModel);

        // Assert
        assertThat(result.getStatusCode().is4xxClientError()).isTrue();
    }

    @Test
    void voteUnsuccessful() {
        // Arrange
        UserChoiceRequestModel requestModel = new UserChoiceRequestModel("1", "username");
        when(this.electionService.hasCurrentlyRunningElection(anyLong())).thenReturn(true);
        when(this.electionService.isCurrentlyRunningInElection(anyString())).thenReturn(false);
        when(this.electionService.voteForElection(anyLong(), anyString(), anyString()))
                .thenReturn(Result.unsuccessful());

        // Act
        ResponseEntity<?> result = this.electionController.vote(1L, requestModel);

        // Assert
        assertThat(result.getStatusCode().is4xxClientError()).isTrue();
    }

    @Test
    void voteSuccessful() {
        // Arrange
        UserChoiceRequestModel requestModel = new UserChoiceRequestModel("1", "username");
        when(this.electionService.hasCurrentlyRunningElection(anyLong())).thenReturn(true);
        when(this.electionService.isCurrentlyRunningInElection(anyString())).thenReturn(false);
        when(this.electionService.voteForElection(anyLong(), anyString(), anyString()))
                .thenReturn(Result.successful());

        // Act
        ResponseEntity<?> result = this.electionController.vote(1L, requestModel);

        // Assert
        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
    }
}