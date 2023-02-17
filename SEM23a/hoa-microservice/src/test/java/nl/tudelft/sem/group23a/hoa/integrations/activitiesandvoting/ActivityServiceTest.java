package nl.tudelft.sem.group23a.hoa.integrations.activitiesandvoting;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import nl.tudelft.sem.group23a.commons.ActivityResponseModel;
import nl.tudelft.sem.group23a.hoa.integrations.activitiesandvoting.models.VotingRequestModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class ActivityServiceTest {

    @Value("${urls.activitiesAndVoting}")
    private String activitiesAndVotingUrl;

    private RestTemplate mockedRestTemplate;
    private ActivityService activityService;

    @BeforeEach
    void setup() {
        mockedRestTemplate = mock(RestTemplate.class);
        activityService = new ActivityService(mockedRestTemplate);
    }

    @Test
    void getActivitiesSendsRequestWithAuthHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("token");
        HttpEntity<VotingRequestModel> request = new HttpEntity<>(headers);
        ResponseEntity<ActivityResponseModel[]> response = new ResponseEntity<>(
                new ActivityResponseModel[] { new ActivityResponseModel()}, null, HttpStatus.OK);

        when(mockedRestTemplate.exchange(activitiesAndVotingUrl + "/activities/future/5",
                HttpMethod.GET,
                request,
                ActivityResponseModel[].class)).thenReturn(response);

        assertThatNoException().isThrownBy(() ->  activityService.getActivities(5, "token", ActivityService.Time.FUTURE));

        verify(mockedRestTemplate, times(1)).exchange(activitiesAndVotingUrl + "/activities/future/5",
                HttpMethod.GET, request, ActivityResponseModel[].class);
    }

    @Test
    void getActivitiesReturnsCorrectly() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("token");
        HttpEntity<VotingRequestModel> request = new HttpEntity<>(headers);
        ResponseEntity<ActivityResponseModel[]> response = new ResponseEntity<>(
                new ActivityResponseModel[] { new ActivityResponseModel()}, null, HttpStatus.OK);

        when(mockedRestTemplate.exchange(activitiesAndVotingUrl + "/activities/future/5",
                HttpMethod.GET,
                request,
                ActivityResponseModel[].class)).thenReturn(response);

        ActivityResponseModel[] result = activityService.getActivities(5, "token", ActivityService.Time.FUTURE);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);

        verify(mockedRestTemplate, times(1)).exchange(activitiesAndVotingUrl + "/activities/future/5",
                HttpMethod.GET, request, ActivityResponseModel[].class);
    }
}
