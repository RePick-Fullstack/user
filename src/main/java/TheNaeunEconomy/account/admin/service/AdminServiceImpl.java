package TheNaeunEconomy.account.admin.service;

import TheNaeunEconomy.account.admin.domain.Admin;
import TheNaeunEconomy.account.admin.repository.AdminRepository;
import TheNaeunEconomy.account.admin.service.request.AddAdminRequest;
import TheNaeunEconomy.account.admin.service.request.DeleteAdminRequest;
import TheNaeunEconomy.account.admin.service.request.LoginAdminRequest;
import TheNaeunEconomy.account.admin.service.request.UpdateAdminRequest;
import TheNaeunEconomy.account.user.service.response.LoginResponse;
import TheNaeunEconomy.jwt.domain.RefreshToken;
import TheNaeunEconomy.jwt.RefreshTokenRepository;
import TheNaeunEconomy.jwt.Token;
import TheNaeunEconomy.jwt.TokenProvider;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
@Transactional
public class AdminServiceImpl implements AdminService {

    @Value("${jwt.ACCESS_TOKEN_MINUTE_TIME}")
    private int ACCESS_TOKEN_MINUTE_TIME;
    @Value("${jwt.REFRESH_TOKEN_MINUTE_TIME}")
    private int REFRESH_TOKEN_MINUTE_TIME;

    public AdminServiceImpl(BCryptPasswordEncoder bCryptPasswordEncoder, TokenProvider tokenProvider,
                            RefreshTokenRepository refreshTokenRepository, AdminRepository adminRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenProvider = tokenProvider;
        this.refreshTokenRepository = refreshTokenRepository;
        this.adminRepository = adminRepository;
    }

    private final AdminRepository adminRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public Admin saveAdmin(AddAdminRequest addAdminRequest) {
        return adminRepository.save(new Admin(addAdminRequest));
    }

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

    @Override
    public Admin updateAdmin(String token, UpdateAdminRequest request) {
        Admin admin = adminRepository.findById(request.getAdminId()).orElseThrow();
        admin.updateUserDetails(request);
        return adminRepository.save(admin);
    }

    @Override
    public void deleteAdmin(String token, DeleteAdminRequest request) {
        tokenProvider.validateToken(token);
        adminRepository.deleteById(request.getAdminId());
    }

    @Override
    public List<Admin> findAllAdmin() {
        return adminRepository.findAll();
    }

    public LoginResponse refreshToken(String refreshToken) {
        System.out.println(refreshToken);
        refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다."));


        refreshTokenRepository.deleteByRefreshToken(refreshToken);

        Admin admin = adminRepository.findById(tokenProvider.getAdminIdFromToken(refreshToken))
                .orElseThrow(() -> new IllegalArgumentException("해당 관리자를 찾을 수 없습니다."));

        Token newAccessToken = tokenProvider.generateToken(admin, ACCESS_TOKEN_MINUTE_TIME);
        Token newRefreshToken = tokenProvider.generateToken(admin, REFRESH_TOKEN_MINUTE_TIME);

        refreshTokenRepository.save(new RefreshToken(admin, newRefreshToken.getToken(),
                LocalDateTime.now().plusMinutes(REFRESH_TOKEN_MINUTE_TIME)));
        return new LoginResponse(newAccessToken, newRefreshToken);
    }
}