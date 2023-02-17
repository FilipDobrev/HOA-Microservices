package nl.tudelft.sem.group23a.notifications.infrastructure.services;

import nl.tudelft.sem.group23a.commons.Result;

/**
 * Service for handling emails.
 */
public interface EmailService {

    /**
     * Sends an email.
     *
     * @param from sender email.
     * @param to receiver email.
     * @param subject The subject of the email.
     * @param textContent The text/plain content of the email.
     * @return Result whether the operation was successful.
     */
    Result sendEmail(String from, String to, String subject, String textContent);
}
