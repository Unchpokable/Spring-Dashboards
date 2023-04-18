package repos;

import Models.Dashboard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDashboardRepository extends JpaRepository<Dashboard, Integer> {
}
