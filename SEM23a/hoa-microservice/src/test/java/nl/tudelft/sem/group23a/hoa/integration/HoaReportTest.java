package nl.tudelft.sem.group23a.hoa.integration;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import nl.tudelft.sem.group23a.commons.utils.JsonUtil;
import nl.tudelft.sem.group23a.hoa.authentication.AuthManager;
import nl.tudelft.sem.group23a.hoa.authentication.JwtTokenVerifier;
import nl.tudelft.sem.group23a.hoa.domain.hoa.MemberId;
import nl.tudelft.sem.group23a.hoa.models.HoaCreationModel;
import nl.tudelft.sem.group23a.hoa.models.HoaJoinModel;
import nl.tudelft.sem.group23a.hoa.models.ReportFilingModel;
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
public class HoaReportTest {

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
        when(mockAuthenticationManager.getId()).thenReturn("ExampleUser");
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
    }

    @Test
    public void setupAndReport() throws Exception {
        // Arrange
        HoaCreationModel creationDto = new HoaCreationModel("hoaname", "Netherlands", "Delft");
        HoaJoinModel joinDto = new HoaJoinModel("Netherlands",
                "Delft",
                "2000",
                "Mekelweg",
                "2");
        ReportFilingModel reportDto = new ReportFilingModel(new MemberId("ExampleUser"),
                "They broke the rules!!!");

        // Act
        // Still include Bearer token as AuthFilter itself is not mocked
        mockMvc.perform(post("/hoa")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(creationDto))
                .header("Authorization", "Bearer MockedToken"));
        mockMvc.perform(put("/hoa/join/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(joinDto))
                .header("Authorization", "Bearer MockedToken"));
        ResultActions result = mockMvc.perform(post("/hoa/report/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(reportDto))
                .header("Authorization", "Bearer MockedToken"));

        // Assert
        result.andExpect(status().isAccepted());
    }

    @Test
    public void setupAndReportUserThatDoesNotExist() throws Exception {
        // Arrange
        HoaCreationModel creationDto = new HoaCreationModel("hoaname", "Netherlands", "Delft");
        HoaJoinModel joinDto = new HoaJoinModel("Netherlands",
                "Delft",
                "2000",
                "Mekelweg",
                "2");
        ReportFilingModel reportDto = new ReportFilingModel(new MemberId("NonExistentUser"),
                "They broke the rules even worse!!!");

        // Act
        // Still include Bearer token as AuthFilter itself is not mocked
        mockMvc.perform(post("/hoa")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(creationDto))
                .header("Authorization", "Bearer MockedToken"));
        mockMvc.perform(put("/hoa/join/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(joinDto))
                .header("Authorization", "Bearer MockedToken"));
        ResultActions result = mockMvc.perform(post("/hoa/report/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(reportDto))
                .header("Authorization", "Bearer MockedToken"));

        // Assert
        result.andExpect(status().isNotFound());
    }

    @Test
    public void setupAndReportInHoaThatWasNotJoined() throws Exception {
        // Arrange
        HoaCreationModel creationDto = new HoaCreationModel("hoaname", "Netherlands", "Delft");
        ReportFilingModel reportDto = new ReportFilingModel(new MemberId("NonExistentUser"),
                "They might have broken the rules!");

        // Act
        // Still include Bearer token as AuthFilter itself is not mocked
        mockMvc.perform(post("/hoa")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(creationDto))
                .header("Authorization", "Bearer MockedToken"));
        ResultActions result = mockMvc.perform(post("/hoa/report/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(reportDto))
                .header("Authorization", "Bearer MockedToken"));

        // Assert
        result.andExpect(status().isForbidden());
    }

    @Test
    public void setupAndReportInHoaThatDoesNotExist() throws Exception {
        // Arrange
        HoaCreationModel creationDto = new HoaCreationModel("hoaname", "Netherlands", "Rotterdam");
        ReportFilingModel reportDto = new ReportFilingModel(new MemberId("NonExistentUser"),
                "Did they break the rules??");

        // Act
        // Still include Bearer token as AuthFilter itself is not mocked
        mockMvc.perform(post("/hoa")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(creationDto))
                .header("Authorization", "Bearer MockedToken"));
        ResultActions result = mockMvc.perform(post("/hoa/report/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(reportDto))
                .header("Authorization", "Bearer MockedToken"));

        // Assert
        result.andExpect(status().isNotFound());
    }
}
