package project.dashboard.models;

import jakarta.persistence.*;
import org.springframework.security.core.parameters.P;
import project.dashboard.internal.ArgumentGuard;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @OneToOne(mappedBy = "owner_id")
    private Long m_id;

    @Column(name = "username")
    private String m_username;

    @Column(name = "first_name")
    private String m_firstName;

    @Column(name = "last_name")
    private String m_lastName;

    @OneToMany(mappedBy = "m_workspace_members")
    private List<Workspace> m_workspaces;

    @OneToMany(mappedBy = "m_dashboard_members")
    private List<Dashboard> m_dashboard;

    public void setUsername(String value) throws IllegalArgumentException
    {
        ArgumentGuard.AssertStringNotNullOrEmpty(value);
        m_username = value;
    }

    public void setFirstName(String value) throws IllegalArgumentException
    {
        ArgumentGuard.AssertStringNotNullOrEmpty(value);
        m_firstName = value;
    }

    public void setLastName(String value) throws IllegalArgumentException
    {
        ArgumentGuard.AssertStringNotNullOrEmpty(value);
        m_lastName = value;
    }

    public Long getId() { return m_id; }

    public String getUsername() { return m_username; }

    public String getFirstName() { return m_firstName; }

    public String getLastName() { return m_lastName; }

    public List<Workspace> getWorkspaces() { return m_workspaces; }

    public List<Workspace> getOwnedWorkspaces()
    {
        return m_workspaces
                .stream()
                .filter(ws -> Objects.equals(ws.getOwner(), m_id))
                .toList();
    }

    public void setM_workspaces(List<Workspace> m_workspaces) {
        this.m_workspaces = m_workspaces;
    }

    public List<Dashboard> getM_dashboard() {
        return m_dashboard;
    }

    public void setM_dashboard(List<Dashboard> m_dashboard) {
        this.m_dashboard = m_dashboard;
    }
}
