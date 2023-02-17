package nl.tudelft.sem.group23a.authentication.domain.user;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Email {
    private final transient String mail;

    /**
     * Constructor for the wrapper Email object.
     *
     * @param email The email to wrap
     */
    public Email(String email) {
        this.mail = validateEmail(email);
    }

    /**
     * Used to validate the email.
     *
     * @param email the string to validate
     * @return the string if it is valid or an exception otherwise
     */

    private String validateEmail(String email) {
        if (email == null || email.equals("")) {
            return email;
        } else if (email.matches("[^@$^&*()+=\\/|]+@.+\\..+") && email.length() < 256) {
            return email;
        }
        throw new IllegalArgumentException("Please enter a valid mail!");
    }

    /**
     * Returns the email as a string.
     *
     * @return The wrapped email in the original string format
     */
    public String toString() {
        return mail;
    }
}
