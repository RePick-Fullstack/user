package TheNaeunEconomy.jwt;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    @Modifying
    @Transactional
    @Query("DELETE FROM RefreshToken r WHERE r.expirationDate < CURRENT_TIMESTAMP")
    void deleteExpiredTokens();

    void deleteById(Long id);

    void deleteByRefreshToken(String refreshToken);


    @Modifying
    @Transactional
    @Query("UPDATE RefreshToken r SET r.expirationDate = :newExpirationDate WHERE r.refreshToken = :refreshToken")
    int updateExpirationDateByToken(@Param("newExpirationDate") LocalDateTime newExpirationDate, @Param("refreshToken") String refreshToken);
}