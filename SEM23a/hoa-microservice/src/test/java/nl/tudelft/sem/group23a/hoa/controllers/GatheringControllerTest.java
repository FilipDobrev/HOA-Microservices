package nl.tudelft.sem.group23a.hoa.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import nl.tudelft.sem.group23a.commons.Result;
import nl.tudelft.sem.group23a.hoa.authentication.AuthManager;
import nl.tudelft.sem.group23a.hoa.domain.services.HoaMembershipService;
import nl.tudelft.sem.group23a.hoa.domain.services.HoaService;
import nl.tudelft.sem.group23a.hoa.integrations.activitiesandvoting.GatheringService;
import nl.tudelft.sem.group23a.hoa.models.VotingRequestModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class GatheringControllerTest {

    private GatheringService gatheringService;
    private HoaMembershipService hoaService;
    private AuthManager authManager;
    private GatheringController gatheringController;

    @BeforeEach
    void setup() {
        this.hoaService = mock(HoaMembershipService.class);
        this.authManager = mock(AuthManager.class);
        this.gatheringService = mock(GatheringService.class);
        this.gatheringController = new GatheringController(this.hoaService, this.gatheringService, this.authManager);
    }

    @Test
    void reactGatheringWithoutBeingMember() {
        // arrange
        long hoaId = 1;
        long gatheringId = 9;
        String username = "username";

        when(this.authManager.getId()).thenReturn(username);
        when(this.hoaService.isMember(hoaId, username)).thenReturn(false);

        // act
        VotingRequestModel requestModel = new VotingRequestModel("Going");
        ResponseEntity<?> response = this.gatheringController.reactGathering(hoaId, gatheringId, requestModel);

        // assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void reactGatheringUnsuccessful() {
        // arrange
        long hoaId = 1;
        long gatheringId = 9;
        String username = "username";
        String choice = "Interested";
        String[] errors = new String[] { "some error" };

        when(this.authManager.getId()).thenReturn(username);
        when(this.hoaService.isMember(hoaId, username)).thenReturn(true);
        when(this.gatheringService.reactToGathering(hoaId, gatheringId, username, choice))
                .thenReturn(Result.unsuccessful(errors));

        // act
        VotingRequestModel requestModel = new VotingRequestModel(choice);
        ResponseEntity<?> response = this.gatheringController.reactGathering(hoaId, gatheringId, requestModel);

        // assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo(errors);
    }

    @Test
    void reactGatheringSuccess() {
        // arrange
        long hoaId = 1;
        long gatheringId = 9;
        String username = "username";
        String choice = "Interested";

        when(this.authManager.getId()).thenReturn(username);
        when(this.hoaService.isMember(hoaId, username)).thenReturn(true);
        when(this.gatheringService.reactToGathering(hoaId, gatheringId, username, choice))
                .thenReturn(Result.successful());

        // act
        VotingRequestModel requestModel = new VotingRequestModel(choice);
        ResponseEntity<?> response = this.gatheringController.reactGathering(hoaId, gatheringId, requestModel);

        // assert
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    }
}
