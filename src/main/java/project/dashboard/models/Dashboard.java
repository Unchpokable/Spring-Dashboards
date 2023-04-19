package project.dashboard.models;

import jakarta.persistence.*;

public class Dashboard {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "title")
    private String m_title;

    @OneToMany(mappedBy = "id")
    private Long m_owner_id;
}
