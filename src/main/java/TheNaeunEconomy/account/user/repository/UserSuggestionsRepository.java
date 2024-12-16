package TheNaeunEconomy.account.user.repository;

import TheNaeunEconomy.account.user.domain.UserSuggestions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSuggestionsRepository extends JpaRepository<UserSuggestions, Long> {
}
