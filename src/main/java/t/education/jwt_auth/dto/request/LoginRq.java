package t.education.jwt_auth.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRq {

    private String username;

    private String password;
}
