package nl.tudelft.sem.group23a.hoa.integration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;
import javax.servlet.http.HttpServletRequest;
import nl.tudelft.sem.group23a.commons.ActivityResponseModel;
import nl.tudelft.sem.group23a.hoa.authentication.AuthManager;
import nl.tudelft.sem.group23a.hoa.authentication.JwtTokenVerifier;
import nl.tudelft.sem.group23a.hoa.controllers.HoaController;
import nl.tudelft.sem.group23a.hoa.domain.hoa.Address;
import nl.tudelft.sem.group23a.hoa.domain.hoa.HoaName;
import nl.tudelft.sem.group23a.hoa.domain.hoa.HomeOwnersAssociation;
import nl.tudelft.sem.group23a.hoa.domain.services.HoaMembershipService;
import nl.tudelft.sem.group23a.hoa.domain.services.HoaReportService;
import nl.tudelft.sem.group23a.hoa.domain.services.HoaService;
import nl.tudelft.sem.group23a.hoa.integrations.activitiesandvoting.ActivityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
@ExtendWith(SpringExtension.class)
// activate profiles to have spring use mocks during auto-injection of certain beans.
@ActiveProfiles({"test", "mockTokenVerifier", "mockAuthenticationManager"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class HoaNoticeBoardTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private transient JwtTokenVerifier mockJwtTokenVerifier;

    @Autowired
    private transient AuthManager mockAuthenticationManager;

    @Test
    void getNoticeboard() throws Exception {

        ActivityService mockActivityService = mock(ActivityService.class);

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getHeader("Authorization")).thenReturn("Bearer 12345");

        String url = "http://localhost:8084/activities/future/1";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("12345");
        //Creating the new header for the rest template that has the authorization
        HttpEntity<String> entity = new HttpEntity<>(headers);

        when(mockActivityService.getActivities(1,
                "12345",
                ActivityService.Time.FUTURE)).thenReturn(new ActivityResponseModel[0]);

        when(mockAuthenticationManager.getId()).thenReturn("ExampleUser");
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);

        HoaMembershipService mockHoaMembershipService = mock(HoaMembershipService.class);
        HoaService mockHoaService = mock(HoaService.class);
        HoaReportService mockHoaReport = mock(HoaReportService.class);
        when(mockHoaMembershipService.allowedToGetNoticeboard(any(Long.class), any(String.class)))
                .thenReturn(true);
        when(mockHoaService.getHoa(1L)).thenReturn(HomeOwnersAssociation.builder()
                .boardMembers(new HashSet<>())
                .members(new HashSet<>())
                .hoaName(new HoaName("Name"))
                .address(new Address("Bulgaria", "Sofia"))
                .id(1)
                .build());

        HoaController controller = new HoaController(mockHoaMembershipService, mockHoaService, mockActivityService,
                mockAuthenticationManager, mockRequest);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        mockMvc.perform(get("http://localhost:8083/hoa/notice-board/{hoa_id}", 1)
                        .with(request -> {
                            request.addHeader("Authorization", "Bearer 12345");
                            return request;
                        }))
                .andExpect(status().isOk());

    }
}
