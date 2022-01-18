package user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import user.domain.User;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UUID, User> {
}
