package TheNaeunEconomy.user.config.jwt;

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
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
    private final JwtProperties jwtProperties;

    public String generateToken(User user) {
        Duration expiredAt = Duration.ofMinutes(30);
        Date now = new Date();
        return makeToken(user, new Date(now.getTime() + expiredAt.toMillis()));
    }

    private String makeToken(User user, Date expiry) {
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .claim("uuid", user.getUuid())
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtProperties.getSecretKey()).parseClaimsJws(token);
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

    public String getUserUuidFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtProperties.getSecretKey()).parseClaimsJws(token).getBody();
        return claims.get("uuid", String.class);
    }
}