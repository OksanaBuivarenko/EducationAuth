package t.education.jwt_auth.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AppControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @LocalServerPort
    private Integer port;

    @Test
    @WithAnonymousUser
    @DisplayName("All access")
    void allAccess() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("http://localhost:" + port + "/api/v1/app/all");
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Public response data"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Admin access")
    void adminAccess() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("http://localhost:" + port + "/api/v1/app/admin");
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Admin response data"));
    }

    @Test
    @WithAnonymousUser
    @DisplayName("Admin access with status 401")
    void adminAccessWithStatus401() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("http://localhost:" + port + "/api/v1/app/admin");
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "manager", roles = {"MANAGER"})
    @DisplayName("Manager access")
    void managerAccess() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("http://localhost:" + port + "/api/v1/app/manager");
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Manager response data"));
    }

    @Test
    @WithAnonymousUser
    @DisplayName("Manager access with status 401")
    void managerAccessWithStatus401() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("http://localhost:" + port + "/api/v1/app/manager");
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("User access")
    void userAccess() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("http://localhost:" + port + "/api/v1/app/user");
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("User response data"));
    }

    @Test
    @WithAnonymousUser
    @DisplayName("User access with status 401")
    void userAccessWithStatus401() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("http://localhost:" + port + "/api/v1/app/user");
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}