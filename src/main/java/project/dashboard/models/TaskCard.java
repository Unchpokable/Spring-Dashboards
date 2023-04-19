package project.dashboard.models;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "cards")
public class TaskCard {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Long m_dashboard_id;

    @OneToOne(mappedBy = "m_dashboard_members")
    private Long m_assignee_id;

    @Column(name = "deadline")
    private Instant m_deadline;

    @Column(name = "description")
    private String m_description;

    @Column(name = "status")
    private String m_status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getM_dashboard_id() {
        return m_dashboard_id;
    }

    public void setM_dashboard_id(Long m_dashboard_id) {
        this.m_dashboard_id = m_dashboard_id;
    }

    public Long getM_assignee_id() {
        return m_assignee_id;
    }

    public void setM_assignee_id(Long m_assignee_id) {
        this.m_assignee_id = m_assignee_id;
    }

    public Instant getM_deadline() {
        return m_deadline;
    }

    public void setM_deadline(Instant m_deadline) {
        this.m_deadline = m_deadline;
    }

    public String getM_description() {
        return m_description;
    }

    public void setM_description(String m_description) {
        this.m_description = m_description;
    }

    public String getM_status() {
        return m_status;
    }

    public void setM_status(String m_status) {
        this.m_status = m_status;
    }
}
