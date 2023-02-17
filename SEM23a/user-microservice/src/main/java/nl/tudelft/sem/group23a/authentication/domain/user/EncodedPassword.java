package nl.tudelft.sem.group23a.authentication.domain.user;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class EncodedPassword {
    public final transient String encoding;

    /**
     * Constructor for the password wrapper object.
     *
     * @param enc the encoded password to wrap
     */
    public EncodedPassword(String enc) {
        encoding = enc;
    }

    /**
     * Returns the string of the encoding.
     *
     * @return The string of the encoded password inside the wrapped object
     */
    public String toString() {
        return encoding;
    }
}
