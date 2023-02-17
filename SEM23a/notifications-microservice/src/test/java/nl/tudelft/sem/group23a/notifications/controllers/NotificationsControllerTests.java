package nl.tudelft.sem.group23a.notifications.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import nl.tudelft.sem.group23a.commons.Result;
import nl.tudelft.sem.group23a.notifications.models.NotificationRequestModel;
import nl.tudelft.sem.group23a.notifications.services.notifications.NotificationsService;
import nl.tudelft.sem.group23a.notifications.services.notifications.models.Notification;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class NotificationsControllerTests {

    @Test
    public void sendNotificationSuccessfully() {
        var notification = new NotificationRequestModel(new String[] { "user1" }, "subject", "content");
        var notificationsService = mock(NotificationsService.class);
        when(notificationsService.sendNotification(any(String[].class), any(Notification.class)))
                .thenReturn(Result.successful());
        var notificationsController = new NotificationsController(notificationsService);

        var result = notificationsController.sendNotification(notification);

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).isEqualTo(null);
    }

    @Test
    void sendNotificationErrors() {
        var notification = new NotificationRequestModel(new String[] { "user1", "user2" }, "subject", "content");
        var notificationsService = mock(NotificationsService.class);
        when(notificationsService.sendNotification(any(String[].class), any(Notification.class)))
                .thenReturn(Result.unsuccessful("Error1", "Error2"));
        var notificationsController = new NotificationsController(notificationsService);

        var result = notificationsController.sendNotification(notification);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat((String[]) result.getBody()).isEqualTo(new String[] { "Error1", "Error2" });
    }
}
