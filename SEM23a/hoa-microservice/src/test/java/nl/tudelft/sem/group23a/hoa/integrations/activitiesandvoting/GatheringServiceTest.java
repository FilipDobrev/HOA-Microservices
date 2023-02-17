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

public class GatheringServiceTest {

    @Value("${urls.activitiesAndVoting}")
    private String activitiesAndVotingUrl;

    private GatheringService gatheringService;
    private RestTemplate mockedRestTemplate;

    @BeforeEach
    void setup() {
        mockedRestTemplate = mock(RestTemplate.class);
        gatheringService = new GatheringService(mockedRestTemplate);
    }

    @Test
    void reactToGatheringSuccess() {
        // arrange
        VotingRequestModel model = new VotingRequestModel("choice", "username");
        HttpEntity<VotingRequestModel> request = new HttpEntity<>(model);
        ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.OK);
        when(mockedRestTemplate.exchange(activitiesAndVotingUrl + "/activities/hoa/1/gatherings/2/vote",
                HttpMethod.PUT,
                request,
                Void.class)).thenReturn(response);

        // act
        Result result = gatheringService.reactToGathering(1, 2, "username", "choice");

        // assert
        assertThat(result.isSuccess()).isTrue();

        verify(mockedRestTemplate, times(1)).exchange(activitiesAndVotingUrl + "/activities/hoa/1/gatherings/2/vote",
                HttpMethod.PUT, request, Void.class);
    }

    @Test
    void reactToGatheringErrorHttpCode() {
        // arrange
        VotingRequestModel model = new VotingRequestModel("choice", "username");
        HttpEntity<VotingRequestModel> request = new HttpEntity<>(model);
        ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        when(mockedRestTemplate.exchange(activitiesAndVotingUrl + "/activities/hoa/1/gatherings/2/vote",
                HttpMethod.PUT,
                request,
                Void.class)).thenReturn(response);

        // act
        Result result = gatheringService.reactToGathering(1, 2, "username", "choice");

        // assert
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getErrors()).hasSameElementsAs(List.of(GatheringService.Errors.UNABLE_TO_REACT_TO_GATHERING));

        verify(mockedRestTemplate, times(1)).exchange(activitiesAndVotingUrl + "/activities/hoa/1/gatherings/2/vote",
                HttpMethod.PUT, request, Void.class);
    }
}
