package project.dashboard.repos;

import org.springframework.data.repository.CrudRepository;
import project.dashboard.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends CrudRepository<User, Long> {

    User findByNickname(String username);
}
