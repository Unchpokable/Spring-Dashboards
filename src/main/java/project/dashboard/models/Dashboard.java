package project.dashboard.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "dashboards")
public class Dashboard {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "title")
    private String m_title;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private Long m_owner_id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ws_id")
    private Long m_workspace_id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "members")
    private List<User> m_dashboard_members;

    @OneToMany(mappedBy = "id", cascade = CascadeType.ALL)
    private List<TaskCard> m_cards;


    public String getTitle()
    {
        return m_title;
    }

    public void setTitle(String value)
    {
        m_title = value;
    }

    public Long getM_owner_id()
    {
        return m_owner_id;
    }

    public void setM_owner_id(Long m_owner_id) {
        this.m_owner_id = m_owner_id;
    }

    public Long getM_workspace_id() {
        return m_workspace_id;
    }

    public void setM_workspace_id(Long m_workspace_id) {
        this.m_workspace_id = m_workspace_id;
    }

    public List<User> getM_dashboard_members() {
        return m_dashboard_members;
    }

    public void setM_dashboard_members(List<User> m_dashboard_members) {
        this.m_dashboard_members = m_dashboard_members;
    }

    public List<TaskCard> getM_cards() {
        return m_cards;
    }

    public void setM_cards(List<TaskCard> m_cards) {
        this.m_cards = m_cards;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
