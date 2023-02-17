package nl.tudelft.sem.group23a.authentication.controllers;

import nl.tudelft.sem.group23a.authentication.authentication.JwtTokenGenerator;
import nl.tudelft.sem.group23a.authentication.authentication.JwtUserDetailsService;
import nl.tudelft.sem.group23a.authentication.domain.exceptions.UsernameIsAlreadyTakenException;
import nl.tudelft.sem.group23a.authentication.domain.services.RegistrationService;
import nl.tudelft.sem.group23a.authentication.domain.user.Email;
import nl.tudelft.sem.group23a.authentication.domain.user.Password;
import nl.tudelft.sem.group23a.authentication.domain.user.Username;
import nl.tudelft.sem.group23a.authentication.models.AuthenticationRequestModel;
import nl.tudelft.sem.group23a.authentication.models.AuthenticationResponseModel;
import nl.tudelft.sem.group23a.authentication.models.RegistrationRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class AuthenticationController {

    private final transient AuthenticationManager authenticationManager;

    private final transient JwtTokenGenerator jwtTokenGenerator;

    private final transient JwtUserDetailsService jwtUserDetailsService;

    private final transient RegistrationService registrationService;

    /**
     * Creates and autowires the controller.
     *
     * @param authenticationManager Manages authentications
     * @param jwtTokenGenerator     Generates authentication token
     * @param jwtUserDetailsService Handles user's data
     * @param registrationService   Handles the registration service
     */
    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager,
                                    JwtTokenGenerator jwtTokenGenerator,
                                    JwtUserDetailsService jwtUserDetailsService,
                                    RegistrationService registrationService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenGenerator = jwtTokenGenerator;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.registrationService = registrationService;
    }

    /**
     * The endpoint receiving the information of the user.
     *
     * @param request the request holds a json format of the user's information.
     * @return a Response entity telling us if everything went okay or not
     * @throws UsernameIsAlreadyTakenException if the username provided in the request is not unique
     */
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegistrationRequestModel request) throws UsernameIsAlreadyTakenException,
            IllegalArgumentException {
        try {
            Username name = new Username(request.getUsername());
            Password pass = new Password(request.getPassword());
            var mail = request.getEmail();
            Email email = new Email(mail);
            registrationService.registerUser(name, pass, email);
        } catch (UsernameIsAlreadyTakenException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    /**
     * This endpoint is used for login with already existing credentials.
     *
     * @param request the input credentials of the user
     * @return the token that verifies the identity of the user
     */
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseModel> login(@RequestBody AuthenticationRequestModel request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()));
        } catch (DisabledException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS", e);
        }

        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(request.getUsername());
        final String jwtToken = jwtTokenGenerator.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponseModel(jwtToken));
    }
}
