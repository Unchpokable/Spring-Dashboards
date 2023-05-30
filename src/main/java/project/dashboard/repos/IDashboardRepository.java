package project.dashboard.repos;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import project.dashboard.models.Dashboard;
import org.springframework.data.jpa.repository.JpaRepository;
import project.dashboard.models.Workspace;

import java.util.List;

public interface IDashboardRepository extends JpaRepository<Dashboard, Long> {
    List<Dashboard> findByWorkspace(Workspace root);
}
