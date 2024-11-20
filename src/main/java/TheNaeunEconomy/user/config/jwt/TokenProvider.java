package TheNaeunEconomy.user.config.jwt;

import TheNaeunEconomy.user.Repository.UserRepository;
import TheNaeunEconomy.user.domain.Token;
import TheNaeunEconomy.user.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import java.time.Duration;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenProvider {
    private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
    private final UserRepository userRepository;

    @Value("${jwt.secret-key}")
    private String jwtSecretKey;

    public Token generateToken(User user, int minutes) {
        Duration expiredAt = Duration.ofMinutes(minutes);
        Date now = new Date();
        String token = makeToken(user, new Date(now.getTime() + expiredAt.toMillis()));
        return new Token(token);
    }

    private String makeToken(User user, Date expiry) {
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .claim("userId", user.getId())
                .claim("nickName", user.getNickname())
                .signWith(SignatureAlgorithm.HS256, jwtSecretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature", e);
        } catch (ExpiredJwtException e) {
            logger.warn("Expired JWT token", e);
        } catch (MalformedJwtException e) {
            logger.error("Malformed JWT token", e);
        } catch (Exception e) {
            logger.error("Invalid JWT token", e);
        }
        return false;
    }

    public Token validateAndReissueAccessToken(String refreshToken) {
        if (!validateToken(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다.");
        }
        Long userIdFromToken = getUserIdFromToken(refreshToken);
        System.out.println("123123" + userIdFromToken);
        User user = userRepository.findById(userIdFromToken)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자 정보입니다."));

        Token accessToken = generateToken(user, 15);

        return accessToken;
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token).getBody();
        System.out.println(claims.get("userId", Long.class));
        return claims.get("userId", Long.class);
    }
}