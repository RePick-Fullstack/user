package TheNaeunEconomy.account.user.repository;


import TheNaeunEconomy.account.user.domain.User;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);

    List<User> findByDeleteDateBeforeAndDeleteDateIsNotNull(LocalDateTime threeMonthsAgo);
}