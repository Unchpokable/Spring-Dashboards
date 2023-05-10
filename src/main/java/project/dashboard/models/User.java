package project.dashboard.models;

import jakarta.persistence.*;
import project.dashboard.internal.ArgumentGuard;
import project.dashboard.internal.FileManager;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "nickname")
    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Workspace> workspaces;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "password")
    private String password;

    @Column(name = "additional_info")
    private String additionalInfo;

    @Lob
    @Column(name = "avatar")
    private byte[] avatar;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();


    // Конструкторы, геттеры и сеттеры
    public User() {}

    public Long getId() { return id; }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() { return password; }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public List<Workspace> getWorkspaces() {
        return workspaces;
    }

    public Set<Role> getRoles() { return roles; }

    public void setNickname(String value) throws IllegalArgumentException {
        ArgumentGuard.AssertStringNotNullOrEmpty(value);
        nickname = value;
    }

    public void setEmail(String value) throws IllegalArgumentException {
        ArgumentGuard.assertEmailIsValid(value);
        email = value;
    }

    public void setFirstName(String value) throws IllegalArgumentException {
        ArgumentGuard.assertStringNotNullOrEmpty(value);
        firstName = value;
    }

    public void setLastName(String value) throws IllegalArgumentException {
        ArgumentGuard.assertStringNotNullOrEmpty(value);
        lastName = value;
    }

    public void setAdditionalInfo(String value) throws IllegalArgumentException {
        ArgumentGuard.assertStringNotNullOrEmpty(value);
        additionalInfo = value;
    }

    public void setAvatar(String filePath) throws IllegalArgumentException {
        ArgumentGuard.assertStringNotNullOrEmpty(filePath);
        try{
            avatar = FileManager.loadFileByteArray(filePath);
        } catch (IOException ex) { }
    }

    public void setRoles(Set<Role> value) {
        roles = value;
    }

    public void setPassword(String value) { password = value;}
}