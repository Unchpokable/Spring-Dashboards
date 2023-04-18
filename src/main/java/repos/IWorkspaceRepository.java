package repos;

import Models.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IWorkspaceRepository extends JpaRepository<Workspace, Long> {
}
