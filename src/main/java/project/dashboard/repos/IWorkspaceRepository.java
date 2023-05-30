package project.dashboard.repos;

import project.dashboard.models.User;
import project.dashboard.models.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IWorkspaceRepository extends JpaRepository<Workspace, Long> {
    Optional<List<Workspace>> findByUser(User user);
}
