package nl.tudelft.sem.group23a.authentication.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import nl.tudelft.sem.group23a.authentication.authentication.JwtTokenGenerator;
import nl.tudelft.sem.group23a.authentication.domain.repositories.UserRepository;
import nl.tudelft.sem.group23a.authentication.domain.services.PasswordEncodingService;
import nl.tudelft.sem.group23a.authentication.domain.user.EncodedPassword;
import nl.tudelft.sem.group23a.authentication.domain.user.HoaUser;
import nl.tudelft.sem.group23a.authentication.domain.user.Password;
import nl.tudelft.sem.group23a.authentication.domain.user.Username;
import nl.tudelft.sem.group23a.authentication.models.AuthenticationRequestModel;
import nl.tudelft.sem.group23a.authentication.models.AuthenticationResponseModel;
import nl.tudelft.sem.group23a.authentication.models.RegistrationRequestModel;
import nl.tudelft.sem.group23a.commons.utils.JsonUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@ExtendWith(SpringExtension.class)
// activate profiles to have spring use mocks during auto-injection of certain beans.
@ActiveProfiles({"test", "mockPasswordEncoder", "mockTokenGenerator", "mockAuthenticationManager"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class HoaUserTests {

    @Autowired
    private transient MockMvc mockMvc;

    @Autowired
    private transient JwtTokenGenerator mockTokenGenerator;
    @Autowired
    private transient PasswordEncodingService mockPasswordEncoder;

    @Autowired
    private transient AuthenticationManager mockAuthManager;

    @Autowired
    private transient UserRepository userRepository;

    RegistrationRequestModel registrationModel;
    final Username testUser = new Username("SomeUser");
    final Password testPassword = new Password("password123");
    final EncodedPassword testHashedPassword = new EncodedPassword("hashedTestPassword");

    /**
     * The setup executed each time we have a registration test.
     */
    public void registrationSetup() {
        when(mockPasswordEncoder.hash(testPassword)).thenReturn(testHashedPassword);

        registrationModel = new RegistrationRequestModel();
        registrationModel.setUsername(testUser.toString());
        registrationModel.setPassword(testPassword.toString());
        registrationModel.setEmail("unturned20@abv.bg");
    }

    /**
     * Tests if the user registration endpoint is working as intended with valid data.
     *
     * @throws Exception Helps us identify what might have gone wrong
     */
    @Test
    public void registerUserValid() throws Exception {
        // Arrange
        registrationSetup();

        // Act
        ResultActions resultActions = mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(registrationModel)));

        HoaUser savedUser = userRepository.findByUsername(testUser).orElseThrow();

        // Assert
        resultActions.andExpect(status().isOk());

        assertThat(savedUser.getUsername()).isEqualTo(testUser);
        assertThat(savedUser.getPassword()).isEqualTo(testHashedPassword);
        assertThat(savedUser.getEmail().toString()).isEqualTo("unturned20@abv.bg");
    }

    /**
     * Tests whether a second user can be registered with the same name.
     *
     * @throws Exception to tell us if the username is already registered or some other exception
     */
    @Test
    public void registerUserAlreadyExisting() throws Exception {
        // Arrange
        registrationSetup();
        // Act
        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(registrationModel)));

        ResultActions resultActions = mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(registrationModel)));

        HoaUser savedUser = userRepository.findByUsername(testUser).orElseThrow();

        // Assert
        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(content().string("SomeUser is already taken!"));

        assertThat(savedUser.getUsername()).isEqualTo(testUser);
        assertThat(savedUser.getPassword()).isEqualTo(testHashedPassword);
        assertThat(savedUser.getEmail().toString()).isEqualTo("unturned20@abv.bg");
    }

    /**
     * Testing if it breaks when we input an invalid username.
     *
     * @throws Exception is supposed to come up telling us that the username is invalid
     */
    @Test
    public void registerUserInvalidUsername() throws Exception {
        // Arrange

        registrationSetup();

        registrationModel.setUsername(null);

        // Act
        ResultActions resultActions = mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(registrationModel)));

        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(content().string("Invalid username pick a new one!"));

        registrationModel.setUsername("");
        resultActions = mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(registrationModel)));

        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(content().string("Invalid username pick a new one!"));
    }

    /**
     * Testing if it breaks when we input an invalid password.
     *
     * @throws Exception is supposed to come up telling us that the password is invalid
     */
    @Test
    public void registerUserInvalidPassword() throws Exception {
        // Arrange

        registrationSetup();

        registrationModel.setPassword(null);

        // Act
        ResultActions resultActions = mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(registrationModel)));

        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(content().string("Invalid password, pick a new one!"));

        registrationModel.setPassword("");
        resultActions = mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(registrationModel)));

        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(content().string("Invalid password, pick a new one!"));
    }

    /**
     * Testing if it breaks when we input an invalid email.
     *
     * @throws Exception is supposed to come up telling us that the email is invalid
     */
    @Test
    public void registerUserInvalidEmail() throws Exception {
        // Arrange

        registrationSetup();

        registrationModel.setEmail("asdasdasdasd@qweqweqwe");

        // Act
        ResultActions resultActions = mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(registrationModel)));

        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(content().string("Please enter a valid mail!"));

    }

    AuthenticationRequestModel authenticationModel;

    /**
     * The simple setup used before starting any login method.
     */
    public void loginSetup() {
        authenticationModel = new AuthenticationRequestModel();
        authenticationModel.setUsername(testUser.toString());
        authenticationModel.setPassword(testPassword.toString());
    }

    /**
     * Validates correct login process.
     *
     * @throws Exception An exception telling us if something went wrong
     */
    @Test
    public void loginValidUser() throws Exception {
        // Arrange

        registerUserValid();
        loginSetup();
        // Act
        when(mockAuthManager.authenticate(argThat(authentication ->
                !testUser.toString().equals(authentication.getPrincipal())
                        || !testPassword.toString().equals(authentication.getCredentials())
        ))).thenThrow(new BadCredentialsException("INVALID_CREDENTIALS"));

        final String testToken = "someToken";
        when(mockTokenGenerator.generateToken(
                argThat(userDetails -> userDetails.getUsername().equals(testUser.toString())))
        ).thenReturn(testToken);

        ResultActions resultActions = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(authenticationModel)));

        resultActions.andExpect(status().isOk());
        MvcResult result = resultActions
                .andExpect(status().isOk())
                .andReturn();

        AuthenticationResponseModel responseModel = JsonUtil.deserialize(result.getResponse().getContentAsString(),
                AuthenticationResponseModel.class);

        assertThat(responseModel.getToken()).isEqualTo(testToken);

        verify(mockAuthManager).authenticate(argThat(authentication ->
                testUser.toString().equals(authentication.getPrincipal())
                        && testPassword.toString().equals(authentication.getCredentials())));
    }

    /**
     * Validates the error handling when invalid information is put.
     *
     * @throws Exception telling us what went wrong if anything
     */
    @Test
    public void loginInvalidUserPassword() throws Exception {
        // Arrange

        registerUserValid();
        loginSetup();
        authenticationModel.setPassword("RandomStuff");
        // Act
        when(mockAuthManager.authenticate(argThat(authentication ->
                !testUser.toString().equals(authentication.getPrincipal())
                        || !testPassword.toString().equals(authentication.getCredentials())
        ))).thenThrow(new BadCredentialsException("INVALID_CREDENTIALS"));

        final String testToken = "someToken";
        when(mockTokenGenerator.generateToken(
                argThat(userDetails -> userDetails.getUsername().equals(testUser.toString())))
        ).thenReturn(testToken);

        ResultActions resultActions = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(authenticationModel)));

        resultActions.andExpect(status().isUnauthorized());
        MvcResult result = resultActions
                .andExpect(status().isUnauthorized())
                .andReturn();

        assertThat(result.getResponse().getErrorMessage()).isEqualTo("INVALID_CREDENTIALS");

    }

}
