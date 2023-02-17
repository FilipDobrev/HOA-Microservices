package nl.tudelft.sem.group23a.notifications.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import nl.tudelft.sem.group23a.notifications.infrastructure.integrations.users.ActualUserService;
import nl.tudelft.sem.group23a.notifications.models.EmailResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class ActualUserServicesTests {

    private RestTemplate mockTemplate;
    private ActualUserService service;

    /**
     * The setup method.
     */
    @BeforeEach
    public void setup() {
        mockTemplate = mock(RestTemplate.class);
        String userUrl = "url";
        service = new ActualUserService(mockTemplate, userUrl);
    }

    @Test
    public void invalidIdentifier() {
        String expected = "Invalid identifier";

        assertThat(service.getEmail("L").getErrors()[0])
                .isEqualTo(expected);
    }

    @Test
    public void badRequest() {
        when(mockTemplate.exchange("url/email/1", HttpMethod.GET, null, EmailResponseModel.class))
                .thenReturn(ResponseEntity.badRequest().build());
        String expected = "Could not get the email of the user";
        assertThat(service.getEmail("1").getErrors()[0])
                .isEqualTo(expected);
    }

    @Test
    public void validRequest() {
        when(mockTemplate.exchange("url/email/1", HttpMethod.GET, null, EmailResponseModel.class))
                .thenReturn(ResponseEntity.ok().body(new EmailResponseModel("dimitar.nikolov2002@gmail.com")));

        String expected = "dimitar.nikolov2002@gmail.com";

        assertThat(service.getEmail("1").getData())
                .isEqualTo(expected);
    }

}
