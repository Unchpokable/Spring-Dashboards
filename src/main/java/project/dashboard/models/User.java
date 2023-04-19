package project.dashboard.models;

import jakarta.persistence.*;
import project.dashboard.internal.ArgumentGuard;

import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long m_id;

    @Column(name = "username")
    private String m_username;

    @Column(name = "first_name")
    private String m_firstName;

    @Column(name = "lastName")
    private String m_lastName;

    @OneToMany(mappedBy = "owner")
    private List<Workspace> m_workspaces;

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
}
