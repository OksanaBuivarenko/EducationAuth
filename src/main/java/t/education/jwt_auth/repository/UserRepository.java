package t.education.jwt_auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import t.education.jwt_auth.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}

