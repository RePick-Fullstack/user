package TheNaeunEconomy.jwt;

import TheNaeunEconomy.jwt.domain.RefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    @Modifying
    @Transactional
    @Query("DELETE FROM RefreshToken r WHERE r.expirationDate < CURRENT_TIMESTAMP")
    void deleteExpiredTokens();

    void deleteById(Long id);

    void deleteByRefreshToken(String refreshToken);

    void deleteByUserId(Long userId);
}