package t.education.jwt_auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import t.education.jwt_auth.dto.request.CreateUserRq;
import t.education.jwt_auth.dto.request.LoginRq;
import t.education.jwt_auth.dto.request.RefreshTokenRq;
import t.education.jwt_auth.dto.response.AuthRs;
import t.education.jwt_auth.entity.RoleType;
import t.education.jwt_auth.security.SecurityService;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.test.context.jdbc.Sql;

@Sql(value = "/sql/init.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/sql/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerTest {

    @Autowired
    private SecurityService securityService;

    @Autowired
    private MockMvc mockMvc;

    @LocalServerPort
    private Integer port;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    ObjectMapper mapper = new ObjectMapper();

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    @DisplayName("Auth user")
    void authUser() throws Exception {
        LoginRq loginRq = LoginRq.builder().username("User").password("12345").build();

        this.mockMvc.perform(post("http://localhost:" + port + "/api/v1/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(loginRq)))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.username").value("User"));
    }

    @Test
    @DisplayName("Register user")
    void registerUser() throws Exception {
        Set<RoleType> users = new HashSet<>();
        users.add(RoleType.ROLE_USER);
        CreateUserRq createUserRq = CreateUserRq.builder()
                .username("User2")
                .email("user2@mail.ru")
                .password("54321")
                .roles(users)
                .build();

        this.mockMvc.perform(post("http://localhost:" + port + "/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createUserRq)))
                .andDo(print())
                .andExpectAll(status().isOk())
                .andExpect(content().string("{\"message\":\"User created!\"}"));
    }

    @Test
    @DisplayName("Refresh token")
    void refreshToken() throws Exception {
        LoginRq loginRq = LoginRq.builder().username("User").password("12345").build();
        AuthRs authRs = securityService.authenticate(loginRq);
        RefreshTokenRq refreshTokenRq = RefreshTokenRq.builder().refreshToken(authRs.getRefreshToken()).build();

        this.mockMvc.perform(post("http://localhost:" + port + "/api/v1/auth/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(refreshTokenRq)))
                .andDo(print())
                .andExpectAll(status().isOk());
    }
}