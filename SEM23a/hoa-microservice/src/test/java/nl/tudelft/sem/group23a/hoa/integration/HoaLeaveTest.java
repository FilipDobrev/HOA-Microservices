package nl.tudelft.sem.group23a.hoa.integration;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import nl.tudelft.sem.group23a.commons.utils.JsonUtil;
import nl.tudelft.sem.group23a.hoa.authentication.AuthManager;
import nl.tudelft.sem.group23a.hoa.authentication.JwtTokenVerifier;
import nl.tudelft.sem.group23a.hoa.models.HoaCreationModel;
import nl.tudelft.sem.group23a.hoa.models.HoaJoinModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@ExtendWith(SpringExtension.class)
// activate profiles to have spring use mocks during auto-injection of certain beans.
@ActiveProfiles({"test", "mockTokenVerifier", "mockAuthenticationManager"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class HoaLeaveTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private transient JwtTokenVerifier mockJwtTokenVerifier;

    @Autowired
    private transient AuthManager mockAuthenticationManager;

    /**
     * Sets up the testing environment before each integration test.
     */
    @BeforeEach
    public void setUp() {
        // Mock the authorization system.
        when(this.mockAuthenticationManager.getId()).thenReturn("ExampleUser");
        when(this.mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
    }

    @Test
    public void setupAndLeaveHoa() throws Exception {
        // Arrange
        HoaCreationModel creationDto = new HoaCreationModel("hoaname", "Netherlands", "Delft");
        HoaJoinModel joinDto = new HoaJoinModel("Netherlands", "Delft", "2000", "Mekelweg", "2");

        // Act
        // Still include Bearer token as AuthFilter itself is not mocked
        this.mockMvc.perform(post("/hoa")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(creationDto))
                .header("Authorization", "Bearer MockedToken"));
        this.mockMvc.perform(put("/hoa/join/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(joinDto))
                .header("Authorization", "Bearer MockedToken"));
        ResultActions result = this.mockMvc.perform(put("/hoa/leave/1")
                .header("Authorization", "Bearer MockedToken"));

        // Assert
        result.andExpect(status().isNoContent());
    }

    @Test
    public void setupAndLeaveHoaThatWasNotJoined() throws Exception {
        // Arrange
        HoaCreationModel creationDto = new HoaCreationModel("hoaname", "Netherlands", "Delft");

        // Act
        // Still include Bearer token as AuthFilter itself is not mocked
        this.mockMvc.perform(post("/hoa")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(creationDto))
                .header("Authorization", "Bearer MockedToken"));
        ResultActions result = this.mockMvc.perform(put("/hoa/leave/1")
                .header("Authorization", "Bearer MockedToken"));

        // Assert
        result.andExpect(status().isNotFound());
    }

    @Test
    public void setupAndLeaveHoaThatDoesntExist() throws Exception {
        // Act
        // Still include Bearer token as AuthFilter itself is not mocked
        ResultActions result = this.mockMvc.perform(put("/hoa/leave/1")
                .header("Authorization", "Bearer MockedToken"));

        // Assert
        result.andExpect(status().isNotFound());
    }
}
