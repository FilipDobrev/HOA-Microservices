package nl.tudelft.sem.group23a.notifications.infrastructure.services;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import java.io.IOException;
import nl.tudelft.sem.group23a.commons.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * EmailService that uses the SendGrid API to send emails.
 */
@Service
@ConditionalOnProperty(
        value = "email.sendgrid.enabled",
        havingValue = "true")
public class SendGridEmailService implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(SendGridEmailService.class);

    private final transient SendGrid sendGridApiWrapper;

    /**
     * Constructor for the service.
     *
     * @param sendGridApiWrapper the SendGrid's API wrapper.
     */
    public SendGridEmailService(SendGrid sendGridApiWrapper) {
        this.sendGridApiWrapper = sendGridApiWrapper;
    }

    @Override
    public Result sendEmail(String from, String to, String subject, String textContent) {
        var fromEmail = new Email(from);
        var toEmail = new Email(to);
        var content = new Content("text/plain", textContent);
        var mail = new Mail(fromEmail, subject, toEmail, content);

        var request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            var response = sendGridApiWrapper.api(request);

            if (response.getStatusCode() != HttpStatus.OK.value()
                    && response.getStatusCode() != HttpStatus.ACCEPTED.value()) {
                logger.error(String.format("Error while trying to send email via SendGrid: %s", response.getBody()));
                return Result.unsuccessful("Something went wrong while trying to send an email.");
            }

            return Result.successful();
        } catch (IOException e) {
            return Result.unsuccessful(e.getMessage());
        }
    }
}
