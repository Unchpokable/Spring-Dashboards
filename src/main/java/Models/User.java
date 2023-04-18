package Models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "users")
public class User {
        @Id
        @GeneratedValue
        private Long m_id;

        @Column(name = "username")
        private String m_username;

        @Column(name = "first_name")
        private String m_firstName;

        @Column(name = "lastName")
        private String m_lastName;

        @Column(name = "workspaces")
        @OneToMany(mappedBy = "owner")
        private List<Workspace> m_workspaces;
}
