package nl.tudelft.sem.group23a.authentication.domain.user;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Username {
    private final transient String name;

    /**
     * Constructor for the username of the user.
     *
     * @param u The name the user has input
     */
    public Username(String u) {
        if (u != null && !u.equals("") && u.length() < 128) {
            name = u;
        } else {
            throw new IllegalArgumentException("Invalid username pick a new one!");
        }
    }

    /**
     * gives us a string format of the name.
     *
     * @return String containing the name
     */
    @Override
    public String toString() {
        return name;
    }
}
