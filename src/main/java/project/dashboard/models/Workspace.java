package project.dashboard.models;

import jakarta.persistence.*;
import project.dashboard.internal.ArgumentGuard;

import java.util.List;

@Entity
@Table(name = "workspaces")
public class Workspace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL)
    private List<Dashboard> boards;

    public Long getId() { return id; }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        ArgumentGuard.assertStringNotNullOrEmpty(value);
        name = value;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User value) {
        user = value;
    }

    public List<Dashboard> getBoards() {
        return boards;
    }

    // Конструкторы, геттеры и сеттеры
}