package nl.tudelft.sem.group23a.notifications.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.AbstractMap;
import org.junit.jupiter.api.Test;

public class SendGridConfigTests {

    @Test
    public void initializedWithCorrectApiKey() {
        var config = new SendGridConfig();
        var apiKey = "api_1556";

        var sendGrid = config.sendGridApiWrapper(apiKey);

        assertThat(sendGrid.getRequestHeaders())
                .contains(new AbstractMap.SimpleEntry<>("Authorization", String.format("Bearer %s", apiKey)));
    }
}
