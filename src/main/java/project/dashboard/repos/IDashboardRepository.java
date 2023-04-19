package project.dashboard.repos;

import project.dashboard.models.Dashboard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDashboardRepository extends JpaRepository<Dashboard, Long> {
}
