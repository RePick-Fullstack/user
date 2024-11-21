package TheNaeunEconomy.user.user.controller;

import TheNaeunEconomy.user.kakao_api.service.request.AccessToken;
import TheNaeunEconomy.user.user.domain.User;
import TheNaeunEconomy.user.user.service.UserServiceImpl;
import TheNaeunEconomy.user.user.service.response.UserNameResponse;
import TheNaeunEconomy.user.user.service.request.UpdateUserRequest;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


    @GetMapping("/name")
    public ResponseEntity<UserNameResponse> getUserName(@RequestHeader HttpHeaders headers) {
        String token = getToken(headers, "Bearer");
        userService.getUserName(token);
        return ResponseEntity.ok(userService.getUserName(token));
    }

    @GetMapping("/logout")
    public HttpStatus logoutUser(@RequestHeader HttpHeaders headers) {
        String token = getToken(headers, "Bearer");
        userService.logoutUser(token);
        return HttpStatus.OK;
    }


    @DeleteMapping("/delete")
    public ResponseEntity<User> deleteUser(@RequestHeader HttpHeaders headers) {
        String token = getToken(headers, "Bearer");
        return ResponseEntity.ok().body(userService.deleteUser(token));
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@Valid @RequestBody UpdateUserRequest request,
                                           @RequestHeader HttpHeaders headers) {
        String token = getToken(headers, "Bearer ");
        return ResponseEntity.ok().body(userService.updateUser(request, token));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AccessToken> validateAndReissueAccessToken(@RequestHeader HttpHeaders headers) {
        String refreshToken = getToken(headers, "Bearer");
        userService.refreshToken(refreshToken);
        return ResponseEntity.ok().body(userService.refreshToken(refreshToken));
    }

    @Nullable
    private static String getToken(HttpHeaders headers, String Bearer) {
        String token = headers.getFirst(HttpHeaders.AUTHORIZATION);
        if (token != null && token.startsWith(Bearer)) {
            token = token.substring(7);
        }
        return token;
    }
}