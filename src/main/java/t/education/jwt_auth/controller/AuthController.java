package t.education.jwt_auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import t.education.jwt_auth.dto.request.CreateUserRq;
import t.education.jwt_auth.dto.request.LoginRq;
import t.education.jwt_auth.dto.request.RefreshTokenRq;
import t.education.jwt_auth.dto.response.AuthRs;
import t.education.jwt_auth.dto.response.RefreshTokenRs;
import t.education.jwt_auth.dto.response.SimpleRs;
import t.education.jwt_auth.security.SecurityService;
import t.education.jwt_auth.service.UserService;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    private final SecurityService securityService;

    @PostMapping("signin")
    public ResponseEntity<AuthRs> authUser(@RequestBody LoginRq loginRq) {
        return ResponseEntity.ok(securityService.authenticate(loginRq));
    }

    @PostMapping("register")
    public ResponseEntity<SimpleRs> registerUser(@RequestBody CreateUserRq createUserRq) {
        if (!userService.isUserExists(createUserRq)) {
            securityService.register(createUserRq);
        }
        return ResponseEntity.ok(new SimpleRs("User created!"));
    }

    @PostMapping("refresh-token")
    public ResponseEntity<RefreshTokenRs> refreshToken(@RequestBody RefreshTokenRq refreshTokenRq) {
        return ResponseEntity.ok(securityService.refreshToken(refreshTokenRq));
    }

    @PostMapping("logout")
    public ResponseEntity<SimpleRs> logoutUser(@AuthenticationPrincipal UserDetails userDetails) {
        securityService.logout();
        return ResponseEntity.ok(new SimpleRs("User logout! Username is: " + userDetails.getUsername()));
    }
}
