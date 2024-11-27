package TheNaeunEconomy.account.admin.repository;

import TheNaeunEconomy.account.admin.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    Admin findByAdminCode(String adminCode);
}
