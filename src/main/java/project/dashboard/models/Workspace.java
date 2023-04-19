package project.dashboard.models;

import jakarta.persistence.*;
import project.dashboard.internal.ArgumentGuard;

import java.util.List;

@Entity
@Table(name = "workspaces")
public class Workspace {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "title")
    private String m_title;

    @OneToMany(mappedBy = "m_workspace_id")
    private List<Dashboard> m_dashboards;

    @ManyToOne
    @JoinColumn(name = "members")
    private List<User> m_members;

    public Long getId() { return id; }

    public String getTitle() { return m_title; }

    public List<Dashboard> getDashboards() { return m_dashboards; }

    public List<User> getMembers() { return m_members; }

    public void setId(Long value) { id = value; }

    public void setTitle(String value) throws IllegalArgumentException
    {
        ArgumentGuard.AssertStringNotNullOrEmpty(value);
        m_title = value;
    }
}
