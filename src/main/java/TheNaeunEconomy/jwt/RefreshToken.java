package TheNaeunEconomy.jwt;

import TheNaeunEconomy.account.domain.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;

@Entity
@Getter
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate;

    public RefreshToken(User user, String refreshToken, LocalDateTime expirationDate) {
        this.user = user;
        this.refreshToken = refreshToken;
        this.expirationDate = expirationDate;
    }

    public RefreshToken() {
    }
}
