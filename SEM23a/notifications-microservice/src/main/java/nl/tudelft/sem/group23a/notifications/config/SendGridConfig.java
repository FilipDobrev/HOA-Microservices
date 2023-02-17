package nl.tudelft.sem.group23a.notifications.config;

import com.sendgrid.SendGrid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SendGrid bean provider.
 */
@Configuration
public class SendGridConfig {

    /**
     * Provides SendGrid API wrapper, created with the corresponding API key from application.properties.
     *
     * @param sendGridApiKey (email.sendgrid.apikey) the API key.
     * @return instance of the wrapper.
     */
    @Bean
    public SendGrid sendGridApiWrapper(@Value("${email.sendgrid.apikey}") String sendGridApiKey) {
        return new SendGrid(sendGridApiKey);
    }
}
