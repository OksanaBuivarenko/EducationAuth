package t.education.jwt_auth.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import t.education.jwt_auth.dto.request.CreateUserRq;
import t.education.jwt_auth.dto.request.LoginRq;
import t.education.jwt_auth.dto.request.RefreshTokenRq;
import t.education.jwt_auth.dto.response.AuthRs;
import t.education.jwt_auth.dto.response.RefreshTokenRs;
import t.education.jwt_auth.entity.RefreshToken;
import t.education.jwt_auth.entity.User;
import t.education.jwt_auth.exception.RefreshTokenException;
import t.education.jwt_auth.security.jwt.JwtUtils;
import t.education.jwt_auth.service.RefreshTokenService;
import t.education.jwt_auth.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final RefreshTokenService refreshTokenService;

    private final UserService userService;

    public AuthRs authenticate(LoginRq loginRq) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRq.getUsername(),
                loginRq.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        return AuthRs.builder()
                .id(userDetails.getId())
                .email(userDetails.getEmail())
                .username(userDetails.getUsername())
                .roles(roles)
                .refreshToken(refreshToken.getToken())
                .token(jwtUtils.generateJwtToken(userDetails))
                .build();
    }

    public void register(CreateUserRq createUserRq) {
        userService.createUser(createUserRq);
    }

    public RefreshTokenRs refreshToken(RefreshTokenRq refreshTokenRq) {
        String requestRefreshToken = refreshTokenRq.getRefreshToken();

        return refreshTokenService.findByRefreshToken(requestRefreshToken)
                .map(refreshTokenService::checkRefreshToken)
                .map(RefreshToken::getUserId)
                .map(userId -> {
                    User tokenOwner = userService.getUserById(userId);
                    String token = jwtUtils.generateTokenFromUsername(tokenOwner.getUsername());
                    return new RefreshTokenRs(token, refreshTokenService.createRefreshToken(userId).getToken());
                }).orElseThrow(() -> new RefreshTokenException(requestRefreshToken, "Refresh token not found"));
    }

    public void logout() {
        var currentPrincipal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (currentPrincipal instanceof AppUserDetails userDetails) {
            Long userId = userDetails.getId();
            refreshTokenService.deleteByUserId(userId);
        }
    }
}