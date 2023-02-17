package nl.tudelft.sem.group23a.activity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Rest bean configuration.
 */
@Configuration
public class RestConfig {

    /**
     * Provides RestTemplate bean for dependency injection.
     *
     * @return the bean
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
