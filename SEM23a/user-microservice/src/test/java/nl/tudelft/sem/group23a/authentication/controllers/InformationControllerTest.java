package nl.tudelft.sem.group23a.authentication.controllers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import nl.tudelft.sem.group23a.authentication.domain.services.InformationService;
import nl.tudelft.sem.group23a.authentication.domain.user.Email;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class InformationControllerTest {

    private InformationService service;

    @BeforeEach
    public void setup() {
        service = mock(InformationService.class);
    }

    @Test
    void email() {
        InformationController controller = new InformationController(service);
        when(service.getEmailById(1L)).thenReturn(new Email(""));
        assertNotNull(controller.email("1"));
    }
}