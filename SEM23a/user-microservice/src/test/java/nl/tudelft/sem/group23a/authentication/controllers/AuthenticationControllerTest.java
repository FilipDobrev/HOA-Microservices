package nl.tudelft.sem.group23a.authentication.controllers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import nl.tudelft.sem.group23a.authentication.authentication.JwtTokenGenerator;
import nl.tudelft.sem.group23a.authentication.authentication.JwtUserDetailsService;
import nl.tudelft.sem.group23a.authentication.domain.exceptions.UsernameIsAlreadyTakenException;
import nl.tudelft.sem.group23a.authentication.domain.services.RegistrationService;
import nl.tudelft.sem.group23a.authentication.models.RegistrationRequestModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;

class AuthenticationControllerTest {

    private AuthenticationManager authenticationManager;

    private JwtTokenGenerator jwtTokenGenerator;

    private JwtUserDetailsService jwtUserDetailsService;

    private RegistrationService registrationService;

    private AuthenticationController controller;

    @BeforeEach
    void setup() {
        authenticationManager = mock(AuthenticationManager.class);
        jwtTokenGenerator = mock(JwtTokenGenerator.class);
        jwtUserDetailsService = mock(JwtUserDetailsService.class);
        registrationService = mock(RegistrationService.class);
        controller = new AuthenticationController(
                authenticationManager,
                jwtTokenGenerator,
                jwtUserDetailsService,
                registrationService);
    }

    @Test
    void register() throws UsernameIsAlreadyTakenException {
        RegistrationRequestModel request = new RegistrationRequestModel();
        request.setUsername("name");
        request.setPassword("pass");
        assertNotNull(controller.register(request));
    }
}