package nl.tudelft.sem.group23a.notifications.services.notifications;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import nl.tudelft.sem.group23a.commons.DataResult;
import nl.tudelft.sem.group23a.commons.Result;
import nl.tudelft.sem.group23a.notifications.infrastructure.integrations.users.UserService;
import nl.tudelft.sem.group23a.notifications.infrastructure.services.EmailService;
import nl.tudelft.sem.group23a.notifications.services.notifications.models.Notification;
import org.junit.jupiter.api.Test;

public class EmailNotificationsServiceTests {

    @Test
    public void sendNotificationToSingleReceiver() {
        var toEmail = "to@email.com";
        var subject = "subject";
        var content = "content";

        var userService = mock(UserService.class);
        when(userService.getEmail("to"))
                .thenReturn(DataResult.successWith(toEmail));
        var emailService = mock(EmailService.class);
        when(emailService.sendEmail(any(), eq(toEmail), any(), any()))
                .thenReturn(Result.successful());
        var emailNotificationsService = new EmailNotificationsService(emailService, userService);

        var notification = new Notification(subject, content);

        var result = emailNotificationsService.sendNotification("to", notification);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getErrors()).isEmpty();
        verify(emailService).sendEmail(any(), eq(toEmail), eq(subject), eq(content));
    }

    @Test
    public void sendNotificationEmailFailed() {
        var toEmail = "to@email.com";
        var subject = "subject";
        var content = "content";

        var userService = mock(UserService.class);
        when(userService.getEmail("to"))
                .thenReturn(DataResult.successWith(toEmail));
        var emailService = mock(EmailService.class);
        when(emailService.sendEmail(any(), eq(toEmail), any(), any()))
                .thenReturn(Result.unsuccessful("emailFailure"));
        var emailNotificationsService = new EmailNotificationsService(emailService, userService);

        var notification = new Notification(subject, content);

        var result = emailNotificationsService.sendNotification("to", notification);

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getErrors()).hasSize(1);
        assertThat(result.getErrors()).contains("emailFailure");
        verify(emailService).sendEmail(any(), eq(toEmail), eq(subject), eq(content));
    }


    @Test
    public void sendNotificationNoEmail() {
        var userService = mock(UserService.class);
        when(userService.getEmail(any()))
                .thenReturn(DataResult.<String>failureWith("userEmailError"));
        var emailService = mock(EmailService.class);
        var emailNotificationsService = new EmailNotificationsService(emailService, userService);

        var result = emailNotificationsService.sendNotification("identifier", new Notification("", ""));

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getErrors()).hasSize(1);
        assertThat(result.getErrors()).contains("userEmailError");
    }
}
