package nl.tudelft.sem.group23a.notifications.infrastructure.integrations.users;

import java.util.Objects;
import nl.tudelft.sem.group23a.commons.DataResult;
import nl.tudelft.sem.group23a.notifications.models.EmailResponseModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Service to simulate communication to users-microservice while it's not implemented.
 */
@Service
public class ActualUserService implements UserService {

    private final transient RestTemplate restTemplate;
    private final transient String userUrl;

    public ActualUserService(RestTemplate restTemplate, @Value("${urls.useremail}") String userUrl) {
        this.restTemplate = restTemplate;
        this.userUrl = userUrl;
    }

    /**
     *  Returns an identifier concatenated with "@email.com".
     *
     * @param identifier user identifier.
     * @return an example email.
     */
    @Override
    public DataResult<String> getEmail(String identifier) {
        if (!identifier.matches("[0-9]+")) {
            return DataResult.failureWith("Invalid identifier");
        }
        var uri = String.format("%s/email/%s", userUrl, identifier);
        var response = restTemplate.exchange(uri, HttpMethod.GET, null, EmailResponseModel.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            return DataResult.failureWith("Could not get the email of the user");
        }

        return DataResult.successWith(Objects.requireNonNull(response.getBody()).getEmail());
    }
}
