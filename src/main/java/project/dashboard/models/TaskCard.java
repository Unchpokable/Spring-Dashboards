package project.dashboard.models;

import jakarta.persistence.*;
import project.dashboard.internal.InstantConverter;
import project.dashboard.models.internals.TaskCardCreationRequestData;

import java.time.Instant;

@Entity
@Table(name = "task_cards")
public class TaskCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "title")
    private String title;

    @Column(nullable = true, name = "description")
    private String description;

    @Column(nullable = false, name = "deadline")
    private Instant deadline;

    @Column(nullable = false, name = "category")
    private TaskCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Dashboard board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_user_id")
    private User assignedUser;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Dashboard getBoard() {
        return board;
    }

    public void setAssignedUser(User assignedUser) {
        this.assignedUser = assignedUser;
    }

    public User getAssignedUser() {
        return assignedUser;
    }

    public Instant getDeadline() {
        return deadline;
    }

    public void setDeadline(Instant deadline) {
        this.deadline = deadline;
    }

    public TaskCategory getCategory() {
        return category;
    }

    public void setCategory(TaskCategory category) {
        this.category = category;
    }

    public void setBoard(Dashboard board) {
        this.board = board;
    }

    public void fillFieldsFromCreationData(TaskCardCreationRequestData data) {
        title = data.getTitle();
        description = data.getDescription();
        deadline = InstantConverter.toInstant(data.getDeadline());
        category = data.getCategory();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
