package nl.tudelft.sem.group23a.activity.integration;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import nl.tudelft.sem.group23a.activity.authentication.JwtTokenVerifier;
import nl.tudelft.sem.group23a.activity.models.ActivityCreationRequestModel;
import nl.tudelft.sem.group23a.commons.ActivityType;
import nl.tudelft.sem.group23a.commons.utils.JsonUtil;
import org.json.JSONArray;
import org.json.JSONObject;
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
@ActiveProfiles({ "test", "mockTokenVerifier" })
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class ActivityCreateAndGetTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private transient JwtTokenVerifier mockJwtTokenVerifier;

    @Test
    void createProposal() throws Exception {
        // Arrange
        // Notice how some custom parts of authorisation need to be mocked.
        // Otherwise, the integration test would never be able to authorise as the authorisation server is offline.
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);

        ActivityCreationRequestModel proposalDto
                = new ActivityCreationRequestModel(1, "Description", ActivityType.PROPOSAL, 3);

        // Act
        // Still include Bearer token as AuthFilter itself is not mocked
        ResultActions result1 = mockMvc.perform(post("/activities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(proposalDto))
                .header("Authorization", "Bearer MockedToken"));

        JSONObject activityCreationResponseJson = new JSONObject();
        activityCreationResponseJson.put("id", 1L);

        // Assert
        result1.andExpect(status().isCreated());
        result1.andExpect(content().json(activityCreationResponseJson.toString()));

        ResultActions result2 = mockMvc.perform(get("/activities/future/1")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer MockedToken"));

        // Assert
        result2.andExpect(status().isOk());
    }

    @Test
    void createGathering() throws Exception {
        // Arrange
        // Notice how some custom parts of authorisation need to be mocked.
        // Otherwise, the integration test would never be able to authorise as the authorisation server is offline.
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);

        ActivityCreationRequestModel proposalDto
                = new ActivityCreationRequestModel(1, "Description", ActivityType.GATHERING, 0);

        // Act
        // Still include Bearer token as AuthFilter itself is not mocked
        ResultActions result1 = mockMvc.perform(post("/activities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(proposalDto))
                .header("Authorization", "Bearer MockedToken"));

        JSONObject activityCreationResponseJson = new JSONObject();
        activityCreationResponseJson.put("id", 1L);

        //Assert
        result1.andExpect(status().isCreated());
        result1.andExpect(content().json(activityCreationResponseJson.toString()));

        ResultActions result2 = mockMvc.perform(get("/activities/past/1")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer MockedToken"));

        // Assert
        result2.andExpect(status().isOk());
    }
}