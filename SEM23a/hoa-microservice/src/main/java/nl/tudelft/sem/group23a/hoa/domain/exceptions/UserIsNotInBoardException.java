package nl.tudelft.sem.group23a.hoa.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class UserIsNotInBoardException extends RuntimeException {

    public UserIsNotInBoardException() {
        super();
    }

    public static final long serialVersionUID = -224317968456658592L;
}
