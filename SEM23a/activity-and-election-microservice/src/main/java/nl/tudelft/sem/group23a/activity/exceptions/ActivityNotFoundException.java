package nl.tudelft.sem.group23a.activity.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ActivityNotFoundException extends RuntimeException {

    public static final long serialVersionUID = -2050328339200458185L;

    public ActivityNotFoundException() {
        super();
    }
}
