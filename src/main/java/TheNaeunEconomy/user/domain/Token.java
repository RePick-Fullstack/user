package TheNaeunEconomy.user.domain;

import lombok.Getter;

@Getter
public class Token {
    private String token;

    public Token(String token) {
        this.token = token;
    }

    public String getValue() {
        return token;
    }
}