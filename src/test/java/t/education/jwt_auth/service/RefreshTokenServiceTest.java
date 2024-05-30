package t.education.jwt_auth.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import t.education.jwt_auth.entity.RefreshToken;
import t.education.jwt_auth.repository.RefreshTokenRepository;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RefreshTokenServiceTest {

    private final RefreshTokenRepository refreshTokenRepository = Mockito.mock(RefreshTokenRepository.class);

    private final RefreshTokenService refreshTokenService = new RefreshTokenService(refreshTokenRepository);

    private RefreshToken refreshToken;

    private RefreshToken savedToken;

    @BeforeEach
    void setUp() {
        refreshToken = RefreshToken.builder()
                .userId(1L)
                .expiredDate(Instant.now().plusMillis(Duration.ofMinutes(30).toMillis()))
                .token(UUID.randomUUID().toString())
                .build();
        savedToken = refreshToken;
        savedToken.setId(1L);
    }

    @AfterEach
    void tearDown() {
        refreshToken = null;
        savedToken = null;
    }

    @Test
    @DisplayName("Find by refresh token")
    void findByRefreshToken() {
        when(refreshTokenRepository.findByToken(refreshToken.getToken())).thenReturn(Optional.ofNullable(savedToken));
        RefreshToken result = refreshTokenService.findByRefreshToken(refreshToken.getToken()).get();
        assertEquals(1L, result.getUserId());
        verify(refreshTokenRepository, times(1)).findByToken(refreshToken.getToken());
    }

    @Test
    @DisplayName("Check valid token")
    void checkRefreshToken() {
        refreshTokenService.checkRefreshToken(savedToken);
        verify(refreshTokenRepository, times(0)).delete(savedToken);
    }

    @Test
    @DisplayName("Delete by user id")
    void deleteByUserId() {
        doNothing().when(refreshTokenRepository).deleteByUserId(1L);
        refreshTokenService.deleteByUserId(1L);
        verify(refreshTokenRepository, times(1)).deleteByUserId(1L);
    }
}