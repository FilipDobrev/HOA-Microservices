package nl.tudelft.sem.group23a.hoa.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class UserIsNotEligibleForBoardException extends RuntimeException {

    public UserIsNotEligibleForBoardException() {
        super();
    }

    public static final long serialVersionUID = 3475868896628987057L;
}
