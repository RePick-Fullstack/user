package TheNaeunEconomy.user.Repository;


import TheNaeunEconomy.user.domain.User;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    User getReferenceById(UUID id);
}