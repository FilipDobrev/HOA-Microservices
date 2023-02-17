package nl.tudelft.sem.group23a.notifications.infrastructure.services;

import nl.tudelft.sem.group23a.commons.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * EmailService implementation that only logs the operations.
 */
@Service
@ConditionalOnProperty(
        value = "email.sendgrid.enabled",
        havingValue = "false",
        matchIfMissing = true)
public class ConsoleEmailService implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(ConsoleEmailService.class);

    @Override
    public Result sendEmail(String from, String to, String subject, String textContent) {
        logger.info("Sent email from {} to {} with subject \"{}\".", from, to, subject);
        return Result.successful();
    }
}
