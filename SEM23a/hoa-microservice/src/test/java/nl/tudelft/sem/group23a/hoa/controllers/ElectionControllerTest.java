package nl.tudelft.sem.group23a.hoa.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import nl.tudelft.sem.group23a.commons.Result;
import nl.tudelft.sem.group23a.hoa.authentication.AuthManager;
import nl.tudelft.sem.group23a.hoa.domain.exceptions.UserIsNotEligibleForBoardException;
import nl.tudelft.sem.group23a.hoa.domain.exceptions.UserIsNotInBoardException;
import nl.tudelft.sem.group23a.hoa.domain.services.HoaMembershipService;
import nl.tudelft.sem.group23a.hoa.integrations.activitiesandvoting.ElectionService;
import nl.tudelft.sem.group23a.hoa.models.VotingRequestModel;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ElectionControllerTest {

    private ElectionService electionService;
    private HoaMembershipService hoaService;
    private AuthManager authManager;
    private ElectionController electionController;

    @BeforeEach
    void setup() {
        this.hoaService = mock(HoaMembershipService.class);
        this.authManager = mock(AuthManager.class);
        this.electionService = mock(ElectionService.class);
        this.electionController = new ElectionController(this.hoaService, this.electionService, this.authManager);
    }

    @Test
    void applyToBoardWithoutBeingMember() {
        // arrange
        long hoaId = 1;
        String username = "username";

        when(this.authManager.getId()).thenReturn(username);
        doThrow(UserIsNotEligibleForBoardException.class).when(this.hoaService).confirmIsEligibleForBoard(hoaId, username);

        // act
        VotingRequestModel requestModel = new VotingRequestModel("Yes");
        ThrowableAssert.ThrowingCallable response = () -> this.electionController.applyForBoard(hoaId, requestModel);

        // assert
        assertThatThrownBy(response).isInstanceOf(UserIsNotEligibleForBoardException.class);
    }

    @Test
    void applyToBoardUnsuccessful() {
        // arrange
        long hoaId = 1;
        String username = "username";
        String choice = "Yes";
        String[] errors = new String[] { "some error" };

        when(this.authManager.getId()).thenReturn(username);
        when(this.hoaService.isMemberForThreePlusYears(hoaId, username)).thenReturn(true);
        when(this.hoaService.isMemberForThreePlusYears(hoaId, username)).thenReturn(true);
        when(this.hoaService.isBoardMemberInOtherHoa(hoaId, username)).thenReturn(false);
        when(this.electionService.applyForElection(hoaId, username, choice)).thenReturn(Result.unsuccessful(errors));

        // act
        VotingRequestModel requestModel = new VotingRequestModel(choice);
        ResponseEntity<?> response = this.electionController.applyForBoard(hoaId, requestModel);

        // assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo(errors);
    }

    @Test
    void applyToBoardSuccess() {
        // arrange
        long hoaId = 1;
        String username = "username";
        String choice = "Yes";

        when(this.authManager.getId()).thenReturn(username);
        when(this.hoaService.isMemberForThreePlusYears(hoaId, username)).thenReturn(true);
        when(this.hoaService.isMemberForThreePlusYears(hoaId, username)).thenReturn(true);
        when(this.hoaService.isBoardMemberInOtherHoa(hoaId, username)).thenReturn(false);
        when(this.electionService.applyForElection(hoaId, username, choice)).thenReturn(Result.successful());

        // act
        VotingRequestModel requestModel = new VotingRequestModel(choice);
        ResponseEntity<?> response = this.electionController.applyForBoard(hoaId, requestModel);

        // assert
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    void voteForBoardWithoutBeingMember() {
        // arrange
        long hoaId = 1;
        String username = "username";

        when(this.authManager.getId()).thenReturn(username);
        when(this.hoaService.isMemberForThreePlusYears(hoaId, username)).thenReturn(false);

        // act
        VotingRequestModel requestModel = new VotingRequestModel("username2");
        ResponseEntity<?> response = this.electionController.voteForBoard(hoaId, requestModel);

        // assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void voteForBoardUnsuccessful() {
        // arrange
        long hoaId = 1;
        String username = "username";
        String choice = "Yes";
        String[] errors = new String[] { "some error" };

        when(this.authManager.getId()).thenReturn(username);
        when(this.hoaService.isMember(hoaId, username)).thenReturn(true);
        when(this.electionService.voteForElection(hoaId, username, choice)).thenReturn(Result.unsuccessful(errors));

        // act
        VotingRequestModel requestModel = new VotingRequestModel(choice);
        ResponseEntity<?> response = this.electionController.voteForBoard(hoaId, requestModel);

        // assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo(errors);
    }

    @Test
    void voteForBoardSuccess() {
        // arrange
        long hoaId = 1;
        String username = "username";
        String votingFor = "username2";

        when(this.authManager.getId()).thenReturn(username);
        when(this.hoaService.isMember(hoaId, username)).thenReturn(true);
        when(this.electionService.voteForElection(hoaId, username, votingFor)).thenReturn(Result.successful());

        // act
        VotingRequestModel requestModel = new VotingRequestModel(votingFor);
        ResponseEntity<?> response = this.electionController.voteForBoard(hoaId, requestModel);

        // assert
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    void applyForBoardAlreadyBoardMember() {
        // arrange
        long hoaId = 1;
        String username = "username";

        when(this.authManager.getId()).thenReturn(username);
        doThrow(UserIsNotEligibleForBoardException.class).when(this.hoaService).confirmIsEligibleForBoard(hoaId, username);

        // act
        VotingRequestModel requestModel = new VotingRequestModel(username);
        ThrowableAssert.ThrowingCallable response = () -> this.electionController.applyForBoard(hoaId, requestModel);

        // assert
        assertThatThrownBy(response).isInstanceOf(UserIsNotEligibleForBoardException.class);
    }

    @Test
    void startNextElectionWithoutBeingBoardMember() {
        // arrange
        long hoaId = 1;
        String username = "username";

        when(this.authManager.getId()).thenReturn(username);
        doThrow(UserIsNotInBoardException.class).when(this.hoaService).confirmIsBoardMember(hoaId, username);

        // act
        ThrowableAssert.ThrowingCallable response = () -> this.electionController.startNextElection(hoaId);

        // assert
        assertThatThrownBy(response).isInstanceOf(UserIsNotInBoardException.class);
    }

    @Test
    void startNextElectionUnsuccessful() {
        // arrange
        long hoaId = 1;
        String username = "username";
        String[] errors = new String[] { "some error" };

        when(this.authManager.getId()).thenReturn(username);
        when(this.hoaService.isBoardMember(hoaId, username)).thenReturn(true);
        when(this.electionService.startElectionProcess(hoaId)).thenReturn(Result.unsuccessful(errors));

        // act
        ResponseEntity<?> response = this.electionController.startNextElection(hoaId);

        // assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo(errors);
    }

    @Test
    void startNextElectionSuccess() {
        // arrange
        long hoaId = 1;
        String username = "username";

        when(this.authManager.getId()).thenReturn(username);
        when(this.hoaService.isBoardMember(hoaId, username)).thenReturn(true);
        when(this.electionService.startElectionProcess(hoaId)).thenReturn(Result.successful());

        // act
        ResponseEntity<?> response = this.electionController.startNextElection(hoaId);

        // assert
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    }
}
