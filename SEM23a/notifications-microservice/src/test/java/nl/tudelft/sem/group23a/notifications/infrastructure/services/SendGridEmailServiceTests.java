package nl.tudelft.sem.group23a.notifications.infrastructure.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import java.io.IOException;
import java.util.HashMap;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

public class SendGridEmailServiceTests {

    @Test
    public void sendEmailUsesApiCorrectly() throws IOException {
        var sendGridWrapper = mock(SendGrid.class);
        when(sendGridWrapper.api(any()))
                .thenReturn(new Response(200, "", new HashMap<>()));
        var sendGridEmailService = new SendGridEmailService(sendGridWrapper);

        var result = sendGridEmailService.sendEmail("from", "to", "subject", "text");

        var argumentCaptor = ArgumentCaptor.forClass(Request.class);
        verify(sendGridWrapper).api(argumentCaptor.capture());

        assertThat(result.isSuccess()).isTrue();
        var sentRequest = argumentCaptor.getValue();
        assertThat(sentRequest.getMethod()).isEqualTo(Method.POST);
        assertThat(sentRequest.getEndpoint()).isEqualTo("mail/send");
        assertThat(sentRequest.getBody()).contains("\"from\":{\"email\":\"from\"}");
        assertThat(sentRequest.getBody()).contains("\"subject\":\"subject\"");
        assertThat(sentRequest.getBody()).contains("\"to\":[{\"email\":\"to\"}]}]");
        assertThat(sentRequest.getBody()).contains("\"content\":[{\"type\":\"text/plain\",\"value\":\"text\"}]}");
    }

    @Test
    public void returnsFailureResultOnException() throws IOException {
        var sendGridWrapper = mock(SendGrid.class);
        when(sendGridWrapper.api(any()))
                .thenThrow(IOException.class);
        var sendGridEmailService = new SendGridEmailService(sendGridWrapper);

        var result = sendGridEmailService.sendEmail("from", "to", "subject", "text");

        assertThat(result.isSuccess()).isFalse();
        assertThat((result.getErrors())).isNotEmpty();
    }

    @Test
    public void returnsFailureResultOnHttpError() throws IOException {
        var sendGridWrapper = mock(SendGrid.class);
        when(sendGridWrapper.api(any()))
                .thenReturn(new Response(400, "", new HashMap<>()));
        var sendGridEmailService = new SendGridEmailService(sendGridWrapper);

        var result = sendGridEmailService.sendEmail("from", "to", "subject", "text");

        assertThat(result.isSuccess()).isFalse();
        assertThat((result.getErrors())).isNotEmpty();
    }
}
