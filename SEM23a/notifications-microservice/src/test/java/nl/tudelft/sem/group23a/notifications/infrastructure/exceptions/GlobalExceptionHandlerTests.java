package nl.tudelft.sem.group23a.notifications.infrastructure.exceptions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

public class GlobalExceptionHandlerTests {

    @Test
    public void formatsErrorsOnMethodArgumentNotValidCorrectly() {
        var handler = new GlobalExceptionHandler();
        var exception = mock(MethodArgumentNotValidException.class);
        var bindingResult = mock(BindingResult.class);
        when(exception.getBindingResult())
                .thenReturn(bindingResult);
        when(bindingResult.getFieldErrors())
                .thenReturn(List.of(new FieldError("objectName", "someField", "defaultMessage")));

        var responseEntity = handler.handleMethodArgumentNotValid(
                exception, mock(HttpHeaders.class), HttpStatus.BAD_REQUEST, mock(WebRequest.class));

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isInstanceOf(List.class);
        assertThat(responseEntity.getBody()).isEqualTo(List.of("Validation error for someField: defaultMessage"));
    }
}
