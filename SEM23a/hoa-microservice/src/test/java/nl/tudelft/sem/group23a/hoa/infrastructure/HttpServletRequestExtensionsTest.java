package nl.tudelft.sem.group23a.hoa.infrastructure;

import static nl.tudelft.sem.group23a.hoa.infrastructure.HttpServletRequestExtensions.getBearerToken;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;

public class HttpServletRequestExtensionsTest {

    @Test
    public void getBearerTokenNullToken() {
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);

        when(mockRequest.getHeader("Authorization"))
                .thenReturn(null);

        assertThat(getBearerToken(mockRequest)).isNull();
    }

    @Test
    public void getBearerTokenCorrectSubstring() {
        var mockRequest = mock(HttpServletRequest.class);

        when(mockRequest.getHeader("Authorization"))
                .thenReturn("Bearer 12354862");

        assertThat(getBearerToken(mockRequest)).isEqualTo("12354862");
    }
}
