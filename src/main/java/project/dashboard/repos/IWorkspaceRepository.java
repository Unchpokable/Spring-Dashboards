package project.dashboard.repos;

import project.dashboard.models.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IWorkspaceRepository extends JpaRepository<Workspace, Long> {
}
