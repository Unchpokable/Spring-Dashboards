package project.dashboard.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "boards")
public class Dashboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    private Workspace workspace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "board_members",
            joinColumns = @JoinColumn(name = "board_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> members = new HashSet<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<TaskCard> taskCards;

    public String getName() {
        return name;
    }

    public void setName(String value) {
        name = value;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public void setWorkspace(Workspace value) {
        workspace = value;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User user) {
        owner = user;
    }

    public Set<User> getMembers() {
        return members;
    }

    public List<TaskCard> getTaskCards() {
        return taskCards;
    }
}