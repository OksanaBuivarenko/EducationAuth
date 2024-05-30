package t.education.jwt_auth.dto.request;

import lombok.Builder;
import lombok.Data;
import t.education.jwt_auth.entity.RoleType;

import java.util.Set;

@Data
@Builder
public class CreateUserRq {

    private String username;

    private String email;

    private Set<RoleType> roles;

    private String password;
}
