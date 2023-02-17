package nl.tudelft.sem.group23a.notifications.infrastructure.integrations.users;

import nl.tudelft.sem.group23a.commons.DataResult;

/**
 * Service to handle communication to users-microservice.
 */
public interface UserService {

    /**
     * Gets the email that corresponds to a specific user identifier.
     *
     * @param identifier user identifier.
     * @return the email result.
     */
    DataResult<String> getEmail(String identifier);
}
