package nl.tudelft.sem.group23a.hoa.integrations.activitiesandvoting;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import nl.tudelft.sem.group23a.commons.Result;
import nl.tudelft.sem.group23a.hoa.integrations.activitiesandvoting.models.ProposalVotingRequestModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class ProposalServiceTest {

    @Value("${urls.activitiesAndVoting}")
    private String activitiesAndVotingUrl;

    private ProposalService proposalService;
    private RestTemplate mockedRestTemplate;

    @BeforeEach
    void setup() {
        mockedRestTemplate = mock(RestTemplate.class);
        proposalService = new ProposalService(mockedRestTemplate);
    }

    @Test
    void voteForProposalSuccess() {
        // arrange
        ProposalVotingRequestModel model = new ProposalVotingRequestModel("choice", "username", 3);
        HttpEntity<ProposalVotingRequestModel> request = new HttpEntity<>(model);
        ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.OK);
        when(mockedRestTemplate.exchange(activitiesAndVotingUrl + "/activities/hoa/1/proposals/2/vote",
                HttpMethod.PUT,
                request,
                Void.class)).thenReturn(response);

        // act
        Result result = proposalService.voteForProposal(1, 2, "username", "choice", 3);

        // assert
        assertThat(result.isSuccess()).isTrue();

        verify(mockedRestTemplate, times(1)).exchange(activitiesAndVotingUrl + "/activities/hoa/1/proposals/2/vote",
                HttpMethod.PUT, request, Void.class);
    }

    @Test
    void voteForProposalErrorHttpCode() {
        // arrange
        ProposalVotingRequestModel model = new ProposalVotingRequestModel("choice", "username", 3);
        HttpEntity<ProposalVotingRequestModel> request = new HttpEntity<>(model);
        ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        when(mockedRestTemplate.exchange(activitiesAndVotingUrl + "/activities/hoa/1/proposals/2/vote",
                HttpMethod.PUT,
                request,
                Void.class)).thenReturn(response);

        // act
        Result result = proposalService.voteForProposal(1, 2, "username", "choice", 3);

        // assert
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getErrors()).hasSameElementsAs(List.of(ProposalService.Errors.UNABLE_TO_VOTE_FOR_PROPOSAL));

        verify(mockedRestTemplate, times(1)).exchange(activitiesAndVotingUrl + "/activities/hoa/1/proposals/2/vote",
                HttpMethod.PUT, request, Void.class);
    }

}
