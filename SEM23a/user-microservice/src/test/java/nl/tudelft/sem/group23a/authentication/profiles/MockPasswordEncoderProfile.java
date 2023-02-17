package nl.tudelft.sem.group23a.authentication.profiles;

import nl.tudelft.sem.group23a.authentication.domain.services.PasswordEncodingService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("mockPasswordEncoder")
@Configuration
public class MockPasswordEncoderProfile {
    /**
     * Mocks the encoding service.
     *
     * @return A mocked service.
     */
    @Bean
    @Primary
    public PasswordEncodingService getMockPasswordEncodingService() {
        return Mockito.mock(PasswordEncodingService.class);
    }
}
