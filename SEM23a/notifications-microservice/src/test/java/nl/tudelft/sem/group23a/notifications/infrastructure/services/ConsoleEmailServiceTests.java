package nl.tudelft.sem.group23a.notifications.infrastructure.services;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class ConsoleEmailServiceTests {

    @Test
    public void returnsSuccess() {
        var consoleEmailService = new ConsoleEmailService();

        var result = consoleEmailService.sendEmail("", "", "", "");

        assertThat(result.isSuccess()).isTrue();
    }
}
