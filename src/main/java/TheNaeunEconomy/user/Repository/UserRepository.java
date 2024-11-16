package TheNaeunEconomy.user.Repository;


import TheNaeunEconomy.user.domain.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUuid(UUID uuid);

    List<User> findByDeleteDateBeforeAndDeleteDateIsNotNull(LocalDateTime threeMonthsAgo);
}