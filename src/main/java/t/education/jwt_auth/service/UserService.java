package t.education.jwt_auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import t.education.jwt_auth.dto.request.CreateUserRq;
import t.education.jwt_auth.entity.User;
import t.education.jwt_auth.exception.AlreadyExistsException;
import t.education.jwt_auth.exception.RefreshTokenException;
import t.education.jwt_auth.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public void createUser(CreateUserRq createUserRq) {
        User user = User.builder()
                .username(createUserRq.getUsername())
                .email(createUserRq.getEmail())
                .password(passwordEncoder.encode(createUserRq.getPassword()))
                .build();
        user.setRoles(createUserRq.getRoles());
        userRepository.save(user);
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new RefreshTokenException("Exception trying to get token for userId: " + userId));
    }

    public boolean isUserExists(CreateUserRq createUserRq) {
        if (userRepository.existsByUsername(createUserRq.getUsername())) {
            throw new AlreadyExistsException("Username already exists!");
        }
        if (userRepository.existsByEmail(createUserRq.getEmail())) {
            throw new AlreadyExistsException("Email already exists!");
        }
        return false;
    }

}
