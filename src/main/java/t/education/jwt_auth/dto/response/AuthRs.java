package t.education.jwt_auth.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AuthRs {

    private Long id;

    private String token;

    private String refreshToken;

    private String username;

    private String email;

    private List<String> roles;
}
