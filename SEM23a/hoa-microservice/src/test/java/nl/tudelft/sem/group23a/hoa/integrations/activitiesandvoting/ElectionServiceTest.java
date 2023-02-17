package nl.tudelft.sem.group23a.hoa.integrations.activitiesandvoting;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import nl.tudelft.sem.group23a.commons.Result;
import nl.tudelft.sem.group23a.hoa.integrations.activitiesandvoting.models.VotingRequestModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class ElectionServiceTest {

    @Value("${urls.activitiesAndVoting}")
    private String activitiesAndVotingUrl;

    private ElectionService electionService;
    private RestTemplate mockedRestTemplate;

    @BeforeEach
    void setup() {
        mockedRestTemplate = mock(RestTemplate.class);
        electionService = new ElectionService(mockedRestTemplate);
    }

    @Test
    void voteForElectionSuccess() {
        // arrange
        VotingRequestModel model = new VotingRequestModel("choice", "username");
        HttpEntity<VotingRequestModel> request = new HttpEntity<>(model);
        ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.OK);
        when(mockedRestTemplate.exchange(activitiesAndVotingUrl + "/elections/1/vote",
                HttpMethod.PUT,
                request,
                Void.class)).thenReturn(response);

        // act
        Result result = electionService.voteForElection(1, "username", "choice");

        // assert
        assertThat(result.isSuccess()).isTrue();

        verify(mockedRestTemplate, times(1)).exchange(activitiesAndVotingUrl + "/elections/1/vote",
                HttpMethod.PUT, request, Void.class);
    }

    @Test
    void voteForElectionErrorHttpCode() {
        // arrange
        VotingRequestModel model = new VotingRequestModel("choice", "username");
        HttpEntity<VotingRequestModel> request = new HttpEntity<>(model);
        ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        when(mockedRestTemplate.exchange(activitiesAndVotingUrl + "/elections/1/vote",
                HttpMethod.PUT,
                request,
                Void.class)).thenReturn(response);

        // act
        Result result = electionService.voteForElection(1, "username", "choice");

        // assert
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getErrors()).hasSameElementsAs(List.of(ElectionService.Errors.UNABLE_TO_VOTE_FOR_HOA));

        verify(mockedRestTemplate, times(1)).exchange(activitiesAndVotingUrl + "/elections/1/vote",
                HttpMethod.PUT, request, Void.class);
    }

    @Test
    void applyForElectionSuccess() {
        // arrange
        VotingRequestModel model = new VotingRequestModel("choice", "username");
        HttpEntity<VotingRequestModel> request = new HttpEntity<>(model);
        ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.OK);
        when(mockedRestTemplate.exchange(activitiesAndVotingUrl + "/elections/1/application",
                HttpMethod.PUT,
                request,
                Void.class)).thenReturn(response);

        // act
        Result result = electionService.applyForElection(1, "username", "choice");

        // assert
        assertThat(result.isSuccess()).isTrue();

        verify(mockedRestTemplate, times(1))
                .exchange(activitiesAndVotingUrl + "/elections/1/application", HttpMethod.PUT, request, Void.class);
    }

    @Test
    void applyForElectionErrorHttpCode() {
        // arrange
        VotingRequestModel model = new VotingRequestModel("choice", "username");
        HttpEntity<VotingRequestModel> request = new HttpEntity<>(model);
        ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        when(mockedRestTemplate.exchange(activitiesAndVotingUrl + "/elections/1/application",
                HttpMethod.PUT,
                request,
                Void.class)).thenReturn(response);

        // act
        Result result = electionService.applyForElection(1, "username", "choice");

        // assert
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getErrors()).hasSameElementsAs(List.of(ElectionService.Errors.UNABLE_TO_APPLY_TO_HOA));

        verify(mockedRestTemplate, times(1))
                .exchange(activitiesAndVotingUrl + "/elections/1/application", HttpMethod.PUT, request, Void.class);
    }

    @Test
    void startElectionProcedureSuccess() {
        // arrange
        ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.OK);
        when(mockedRestTemplate.exchange(activitiesAndVotingUrl + "/elections/1",
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                Void.class)).thenReturn(response);

        // act
        Result result = electionService.startElectionProcess(1);

        // assert
        assertThat(result.isSuccess()).isTrue();

        verify(mockedRestTemplate, times(1))
                .exchange(activitiesAndVotingUrl + "/elections/1", HttpMethod.PUT, HttpEntity.EMPTY, Void.class);
    }

    @Test
    void startElectionProcedureErrorHttpCode() {
        // arrange
        ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        when(mockedRestTemplate.exchange(activitiesAndVotingUrl + "/elections/1",
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                Void.class)).thenReturn(response);

        // act
        Result result = electionService.startElectionProcess(1);

        // assert
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getErrors()).hasSameElementsAs(List.of(ElectionService.Errors.UNABLE_TO_START_ELECTION));

        verify(mockedRestTemplate, times(1))
                .exchange(activitiesAndVotingUrl + "/elections/1", HttpMethod.PUT,  HttpEntity.EMPTY, Void.class);
    }
}
