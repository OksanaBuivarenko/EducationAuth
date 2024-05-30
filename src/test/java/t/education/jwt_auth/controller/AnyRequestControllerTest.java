package t.education.jwt_auth.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
class AnyRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @LocalServerPort
    private Integer port;

    @Test
    @WithAnonymousUser
    @DisplayName("Test hello with status 401")
    void helloWithStatus401() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("http://localhost:" + port + "/api/v1/any/hello");
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser("Mary")
    @DisplayName("Test hello with status 200")
    void hello() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("http://localhost:" + port + "/api/v1/any/hello");
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, Mary!"));
    }
}