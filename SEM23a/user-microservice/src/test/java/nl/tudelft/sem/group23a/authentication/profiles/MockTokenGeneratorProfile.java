package nl.tudelft.sem.group23a.authentication.profiles;

import nl.tudelft.sem.group23a.authentication.authentication.JwtTokenGenerator;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;


@Profile("mockTokenGenerator")
@Configuration
public class MockTokenGeneratorProfile {

    /**
     * Mocks the TokenGenerator.
     *
     * @return A mocked TokenGenerator.
     */
    @Bean
    @Primary  // marks this bean as the first bean to use when trying to inject a TokenGenerator
    public JwtTokenGenerator getMockTokenGenerator() {
        return Mockito.mock(JwtTokenGenerator.class);
    }
}
