package project.dashboard.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dashboard.internal.ArgumentGuard;
import project.dashboard.models.Dashboard;
import project.dashboard.models.TaskCard;
import project.dashboard.models.User;
import project.dashboard.repos.ICardRepository;
import project.dashboard.repos.IDashboardRepository;

import java.time.Instant;

@Service
public class TaskCardService {
    @Autowired
    private ICardRepository cardRepository;

    @Autowired
    private IDashboardRepository dashboardRepository;

    @Transactional
    public void createTaskCard(Dashboard target, String title, String description) {
        createTaskCard(target, title, description, null, null);
    }

    @Transactional
    public void createTaskCard(Dashboard target, String title, String description, User assignee) {
        createTaskCard(target, title, description, assignee, null);
    }

    @Transactional
    public void createTaskCard(Dashboard target, String title, String description, User assignee, Instant deadline) {
        ArgumentGuard.assertNotNull(target);

        var card = new TaskCard();
        card.setTitle(title);
        card.setDescription(description);
        card.setAssignedUser(assignee);
        card.setDeadline(deadline);

        target.getTaskCards().add(card);

        dashboardRepository.save(target);
        cardRepository.save(card);
    }

    @Transactional
    public void removeTaskCad(Dashboard target, TaskCard card) {
        ArgumentGuard.assertNotNull(target, card);

        cardRepository.delete(card);
        target.getTaskCards().remove(card);
        dashboardRepository.save(target);
    }

    @Transactional
    public void editTaskCardTitle(TaskCard card, String title) {
        ArgumentGuard.assertNotNull(card);
        card.setTitle(title);
        cardRepository.save(card);
    }

    @Transactional
    public void editTaskCardDescription(TaskCard card, String description) {
        ArgumentGuard.assertNotNull(card);

        card.setDescription(description);
        cardRepository.save(card);
    }

    @Transactional
    public void editTaskCardAssignee(TaskCard card, User assignee) {
        ArgumentGuard.assertNotNull(card);

        card.setAssignedUser(assignee);
        cardRepository.save(card);
    }

    @Transactional
    public void editTaskCardDeadline(TaskCard card, Instant deadline) {
        ArgumentGuard.assertNotNull(card);

        card.setDeadline(deadline);
        cardRepository.save(card);
    }
}
