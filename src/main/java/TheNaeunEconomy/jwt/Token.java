package TheNaeunEconomy.jwt;

import lombok.Getter;

@Getter
public class Token {
    private String token;

    public Token(String token) {
        this.token = token;
    }
}