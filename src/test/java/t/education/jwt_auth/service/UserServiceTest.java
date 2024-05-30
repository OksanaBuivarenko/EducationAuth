package t.education.jwt_auth.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;
import t.education.jwt_auth.dto.request.CreateUserRq;
import t.education.jwt_auth.entity.RoleType;
import t.education.jwt_auth.entity.User;
import t.education.jwt_auth.repository.UserRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);

    private final PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);

    private final UserService userService = new UserService(userRepository, passwordEncoder);


    private User user;
    private User admin;
    private Set<RoleType> users;
    private Set<RoleType> admins;

    @BeforeEach
    void setUp() {
        users = new HashSet<>();
        users.add(RoleType.ROLE_USER);
        user = User.builder()
                .username("User")
                .password("12345")
                .email("user@mail.ru")
                .roles(users)
                .build();

        admins = new HashSet<>();
        admins.add(RoleType.ROLE_ADMIN);
        admin = User.builder()
                .id(1L)
                .username("Admin")
                .password("54321")
                .email("admin@mail.ru")
                .roles(admins)
                .build();
    }

    @AfterEach
    void tearDown() {
        user = null;
        admin = null;
        users = null;
        admins = null;
    }

    @Test
    @DisplayName("Create user")
    void createUser() {
        String pass = "12345";
        CreateUserRq createUserRq = CreateUserRq.builder()
                .username("User")
                .password(pass)
                .email("user@mail.ru")
                .roles(users)
                .build();
        when(passwordEncoder.encode(pass)).thenReturn("12345");
        when(userRepository.save(user)).thenReturn(user);
        userService.createUser(createUserRq);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Get user by id")
    void getUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(admin));
        User userTest = userService.getUserById(1L);
        assertEquals("Admin", userTest.getUsername());
        assertEquals("admin@mail.ru", userTest.getEmail());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Is user exists false")
    void isUserExistsFalse() {
        Set<RoleType> managers = new HashSet<>();
        managers.add(RoleType.ROLE_MANAGER);
        CreateUserRq createUserRq = CreateUserRq.builder()
                .username("Manager")
                .password("67890")
                .email("manager@mail.ru")
                .roles(managers)
                .build();
        when(userRepository.existsByUsername("Manager")).thenReturn(false);
        when(userRepository.existsByEmail("manager@mail.ru")).thenReturn(false);
        assertFalse(userService.isUserExists(createUserRq));
    }
}