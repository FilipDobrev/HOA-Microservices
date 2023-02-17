package nl.tudelft.sem.group23a.authentication.domain.user;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Password {

    private final transient String pass;

    /**
     * A constructor for the password wrapper object.
     *
     * @param pass the password of the user
     */
    public Password(String pass) {
        if (pass != null && !pass.equals("") && pass.length() < 128) {
            this.pass = pass;
        } else {
            throw new IllegalArgumentException("Invalid password, pick a new one!");
        }
    }

    /**
     * Method to extract the string version of the password.
     *
     * @return the string which is the password
     */
    @Override
    public String toString() {
        return pass;
    }
}
