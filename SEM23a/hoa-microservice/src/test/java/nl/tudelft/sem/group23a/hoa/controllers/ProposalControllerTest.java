package nl.tudelft.sem.group23a.hoa.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import nl.tudelft.sem.group23a.commons.Result;
import nl.tudelft.sem.group23a.hoa.authentication.AuthManager;
import nl.tudelft.sem.group23a.hoa.domain.hoa.HoaBoardService;
import nl.tudelft.sem.group23a.hoa.domain.services.HoaMembershipService;
import nl.tudelft.sem.group23a.hoa.domain.services.HoaService;
import nl.tudelft.sem.group23a.hoa.integrations.activitiesandvoting.ProposalService;
import nl.tudelft.sem.group23a.hoa.models.VotingRequestModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ProposalControllerTest {
    private ProposalService proposalService;
    private HoaMembershipService hoaService;
    private HoaBoardService hoaBoardService;
    private AuthManager authManager;
    private ProposalController proposalController;

    @BeforeEach
    void setup() {
        this.hoaService = mock(HoaMembershipService.class);
        this.hoaBoardService = mock(HoaBoardService.class);
        this.authManager = mock(AuthManager.class);
        this.proposalService = mock(ProposalService.class);
        this.proposalController = new ProposalController(
                this.hoaService, this.proposalService, this.hoaBoardService, this.authManager);
    }

    @Test
    void voteForProposalWithoutBeingBoardMember() {
        // arrange
        long hoaId = 1;
        long proposalId = 5;
        String username = "username";

        when(this.authManager.getId()).thenReturn(username);
        when(this.hoaService.isBoardMember(hoaId, username)).thenReturn(false);

        // act
        VotingRequestModel requestModel = new VotingRequestModel("Yes");
        ResponseEntity<?> response = this.proposalController.voteProposal(hoaId, proposalId, requestModel);

        // assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void voteForProposalUnsuccessful() {
        // arrange
        long hoaId = 1;
        long proposalId = 5;
        String username = "username";
        String choice = "Yes";
        String[] errors = new String[] { "some error" };
        List<String> board = List.of("1", "2", "3");

        when(this.authManager.getId()).thenReturn(username);
        when(this.hoaService.isBoardMember(hoaId, username)).thenReturn(true);
        when(this.hoaBoardService.getBoard(hoaId)).thenReturn(board);
        when(this.proposalService.voteForProposal(hoaId, proposalId, username, choice, board.size()))
                .thenReturn(Result.unsuccessful(errors));

        // act
        VotingRequestModel requestModel = new VotingRequestModel(choice);
        ResponseEntity<?> response = this.proposalController.voteProposal(hoaId, proposalId, requestModel);

        // assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo(errors);
    }

    @Test
    void voteForProposalSuccess() {
        // arrange
        long hoaId = 1;
        long proposalId = 5;
        String username = "username";
        String choice = "Yes";
        List<String> board = List.of("1", "2", "3");

        when(this.authManager.getId()).thenReturn(username);
        when(this.hoaService.isBoardMember(hoaId, username)).thenReturn(true);
        when(this.hoaBoardService.getBoard(hoaId)).thenReturn(board);
        when(this.proposalService.voteForProposal(hoaId, proposalId, username, choice, board.size()))
                .thenReturn(Result.successful());

        // act
        VotingRequestModel requestModel = new VotingRequestModel(choice);
        ResponseEntity<?> response = this.proposalController.voteProposal(hoaId, proposalId, requestModel);

        // assert
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    }
}
