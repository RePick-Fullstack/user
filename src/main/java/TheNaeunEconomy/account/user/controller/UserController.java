package TheNaeunEconomy.account.user.controller;

import TheNaeunEconomy.account.user.service.UserServiceImpl;
import TheNaeunEconomy.account.user.service.response.LoginResponse;
import TheNaeunEconomy.account.user.service.request.UpdateUserRequest;
import TheNaeunEconomy.account.user.service.response.UserMyPageResponse;
import TheNaeunEconomy.account.user.service.response.UserNickNameResponse;
import TheNaeunEconomy.account.user.service.response.UserResponse;
import TheNaeunEconomy.jwt.TokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserServiceImpl userService;
    private final TokenProvider tokenProvider;

    @GetMapping("/name")
    public ResponseEntity<UserNickNameResponse> userName(@RequestHeader HttpHeaders headers) {
        String token = tokenProvider.getToken(headers);
        return ResponseEntity.ok(userService.getUserName(token));
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logoutUser(@RequestHeader HttpHeaders headers) {
        String token = tokenProvider.getToken(headers);
        userService.logoutUser(token);
        return ResponseEntity.ok().body("로그아웃 되었습니다.");
    }


    @DeleteMapping("/delete")
    public ResponseEntity<UserResponse> deleteUser(@RequestHeader HttpHeaders headers) {
        String token = tokenProvider.getToken(headers);
        return ResponseEntity.ok().body(new UserResponse(userService.deleteUser(token)));
    }

    @PutMapping("/update")
    public ResponseEntity<UserResponse> updateUser(@RequestHeader HttpHeaders headers,
                                                   @RequestBody @Valid UpdateUserRequest request) {
        String token = tokenProvider.getToken(headers);
        return ResponseEntity.ok().body(new UserResponse(userService.updateUser(request, token)));
    }

    @GetMapping("/mypage")
    public ResponseEntity<UserMyPageResponse> myPage(@RequestHeader HttpHeaders headers) {
        String token = tokenProvider.getToken(headers);
        return ResponseEntity.ok().body(userService.getUserInfo(token));
    }

    @PostMapping("/check/password")
    public BodyBuilder checkPassword(@RequestHeader HttpHeaders headers, @RequestBody String password ) {
        System.out.println(headers);
        String token = tokenProvider.getToken(headers);
        System.out.println(token);
        if (userService.checkPassword(token, password).equals("ok")){
            return ResponseEntity.ok();
        }
        return ResponseEntity.status(400);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponse> validateAndReissueAccessToken(@RequestHeader HttpHeaders headers) {
        String refreshToken = tokenProvider.getToken(headers);
        return ResponseEntity.ok().body(userService.refreshToken(refreshToken));
    }
}