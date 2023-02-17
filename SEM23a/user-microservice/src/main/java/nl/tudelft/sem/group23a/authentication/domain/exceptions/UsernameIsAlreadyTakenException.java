package nl.tudelft.sem.group23a.authentication.domain.exceptions;

import nl.tudelft.sem.group23a.authentication.domain.user.Username;

public class UsernameIsAlreadyTakenException extends Exception {
    static final long serialVersionUID = -3387516993124229948L;

    public UsernameIsAlreadyTakenException(Username name) {
        super(name.toString() + " is already taken!");
    }
}
