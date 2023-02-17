package nl.tudelft.sem.group23a.notifications.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import nl.tudelft.sem.group23a.commons.DataResult;
import nl.tudelft.sem.group23a.commons.Result;
import nl.tudelft.sem.group23a.notifications.controllers.NotificationsController;
import nl.tudelft.sem.group23a.notifications.infrastructure.integrations.users.UserService;
import nl.tudelft.sem.group23a.notifications.infrastructure.services.EmailService;
import nl.tudelft.sem.group23a.notifications.services.notifications.EmailNotificationsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(properties = { "email.sendgrid.enabled=false", "email.from=email@test.com" })
@ExtendWith(SpringExtension.class)
@ActiveProfiles({"test"})
@AutoConfigureMockMvc
public class NotificationsTests {

    private final transient MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    public NotificationsTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    public void sendNotificationsSuccessfully() throws Exception {
        UserService mockUser = mock(UserService.class);
        EmailService mockEmail = mock(EmailService.class);
        MockMvc mockMvc1 = MockMvcBuilders
                .standaloneSetup(new NotificationsController(new EmailNotificationsService(mockEmail, mockUser)))
                .build();
        when(mockUser.getEmail("1"))
                .thenReturn(DataResult.successWith("test1@gmail.com"));
        when(mockUser.getEmail("2"))
                .thenReturn(DataResult.successWith("test2@gmail.com"));
        when(mockEmail.sendEmail(null, "test1@gmail.com", "A notification", "hmm here is another email"))
                .thenReturn(Result.successful());
        when(mockEmail.sendEmail(null, "test2@gmail.com", "A notification", "hmm here is another email"))
                .thenReturn(Result.successful());
        ResultActions result = mockMvc1.perform(post("/notifications/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n"
                        + "    \"usernames\": [ \"1\", \"2\" ],\n"
                        + "    \"subject\": \"A notification\",\n"
                        + "    \"content\": \"hmm here is another email\"\n"
                        + "}"));

        result.andExpect(status().isOk());

        String response = result.andReturn().getResponse().getContentAsString();
        assertThat(response).isEmpty();
    }

    @Test
    public void sendNotificationsWithoutUsernames() throws Exception {
        ResultActions result = mockMvc.perform(post("/notifications/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n"
                        + "    \"usernames\": [],\n"
                        + "    \"subject\": \"A notification\",\n"
                        + "    \"content\": \"hmm here is another email\"\n"
                        + "}"));

        result.andExpect(status().isBadRequest());

        String response = result.andReturn().getResponse().getContentAsString();
        assertThat(response).isEqualTo("[\"Validation error for usernames: must not be empty\"]");
    }

    @Test
    public void sendNotificationsWithoutSubject() throws Exception {
        ResultActions result = mockMvc.perform(post("/notifications/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n"
                        + "    \"usernames\": [ \"1\" ],\n"
                        + "    \"content\": \"hmm here is another email\"\n"
                        + "}"));

        result.andExpect(status().isBadRequest());

        String response = result.andReturn().getResponse().getContentAsString();
        assertThat(response).isEqualTo("[\"Validation error for subject: must not be null\"]");
    }
}