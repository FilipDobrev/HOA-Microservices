package nl.tudelft.sem.group23a.activity.authentication;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthManagerTests {

    private transient AuthManager authManager;

    @BeforeEach
    void setup() {
        authManager = new AuthManager();
    }

    @Test
    void getMemberIdTest() {
        String expected = "member123";
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                expected,
                null,
                List.of()
        );

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        String actual = authManager.getId();

        assertThat(actual).isEqualTo(expected);
    }
}
