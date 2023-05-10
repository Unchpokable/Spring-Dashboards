package project.dashboard.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import project.dashboard.models.Dashboard;
import project.dashboard.models.TaskCard;

import java.util.List;
import java.util.Optional;

public interface ICardRepository extends JpaRepository<TaskCard, Long> {
    Optional<List<TaskCard>> findByBoard(Dashboard dashboard);
}
