package TheNaeunEconomy.account.admin.service;

import TheNaeunEconomy.account.admin.domain.Admin;
import TheNaeunEconomy.account.admin.repository.AdminRepository;
import TheNaeunEconomy.account.admin.service.request.LoginAdminRequest;
import TheNaeunEconomy.account.user.service.response.LoginResponse;
import TheNaeunEconomy.jwt.RefreshTokenRepository;
import TheNaeunEconomy.jwt.Token;
import TheNaeunEconomy.jwt.TokenProvider;
import TheNaeunEconomy.jwt.domain.RefreshToken;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NonAdminServiceImpl implements NonAdminService {

    @Value("${jwt.ACCESS_TOKEN_MINUTE_TIME}")
    private int ACCESS_TOKEN_MINUTE_TIME;
    @Value("${jwt.REFRESH_TOKEN_MINUTE_TIME}")
    private int REFRESH_TOKEN_MINUTE_TIME;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AdminRepository adminRepository;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public LoginResponse login(LoginAdminRequest loginAdminRequest) {
        Admin admin = adminRepository.findByAdminCode(loginAdminRequest.getAdminCode()).orElseThrow();

        if (bCryptPasswordEncoder.encode(loginAdminRequest.getPassword()).equals(admin.getPassword())) {
            throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
        }

        Token accessToken = tokenProvider.generateToken(admin, ACCESS_TOKEN_MINUTE_TIME);
        Token refreshToken = tokenProvider.generateToken(admin, REFRESH_TOKEN_MINUTE_TIME);

        refreshTokenRepository.save(new RefreshToken(admin, refreshToken.getToken(),
                LocalDateTime.now().plusMinutes(REFRESH_TOKEN_MINUTE_TIME)));
        return new LoginResponse(accessToken, refreshToken);
    }
}