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
    private List<User> m_workspace_members;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private Long m_owner;

    public Long getId() { return id; }

    public String getTitle() { return m_title; }

    public List<Dashboard> getDashboards() { return m_dashboards; }

    public List<User> getMembers() { return m_workspace_members; }

    public Long getOwner() { return m_owner; }

    public void setId(Long value) { id = value; }

    public void setTitle(String value) throws IllegalArgumentException
    {
        ArgumentGuard.AssertStringNotNullOrEmpty(value);
        m_title = value;
    }

    public void setM_dashboards(List<Dashboard> m_dashboards) {
        this.m_dashboards = m_dashboards;
    }

    public void setM_workspace_members(List<User> m_workspace_members) {
        this.m_workspace_members = m_workspace_members;
    }
}
