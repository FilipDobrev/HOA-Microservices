package nl.tudelft.sem.group23a.hoa.integrations.activitiesandvoting;

import lombok.RequiredArgsConstructor;
import nl.tudelft.sem.group23a.commons.ActivityResponseModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Integration service for activities.
 */
@Service
@RequiredArgsConstructor
public class ActivityService {

    private final transient RestTemplate restTemplate;

    @Value("${urls.activitiesAndVoting}")
    private transient String activitiesAndVotingUrl;

    /**
     * Sends a request to another microservice to fetch activities of a specific hoa.
     * The method returns activities of the specified time.
     *
     * @param hoaId the id of the hoa
     * @param userToken the user bearer token
     * @param time activities time
     * @return the activities
     */
    public ActivityResponseModel[] getActivities(long hoaId, String userToken, Time time) {
        String uri = String.format("%s/activities/%s/%s", activitiesAndVotingUrl, time.uriPart, hoaId);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(userToken);
        HttpEntity<?> request = new HttpEntity<>(headers);

        ResponseEntity<ActivityResponseModel[]> response = this.restTemplate.exchange(
                uri,
                HttpMethod.GET,
                request,
                ActivityResponseModel[].class);

        return response.getBody();
    }

    /**
     * Enum representing the time of activities.
     */
    public enum Time {
        FUTURE("future"),
        PAST("past");

        /**
         * Time specifier part of the uri for fetching activities.
         */
        public final String uriPart;

        Time(String uriPart) {
            this.uriPart = uriPart;
        }
    }
}
