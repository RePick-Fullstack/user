package TheNaeunEconomy.account.user.service.request;

import lombok.Getter;

@Getter
public class AddSuggestionsRequest {
    private String name;
    private String content;

    public AddSuggestionsRequest() {
    }

    public AddSuggestionsRequest(String name, String content) {
        this.name = name;
        this.content = content;
    }
}