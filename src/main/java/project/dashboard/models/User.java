package project.dashboard.models;

import jakarta.persistence.*;
import project.dashboard.internal.ArgumentGuard;
import project.dashboard.internal.FileManager;

import java.io.IOException;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column(name = "additional_info")
    private String additionalInfo;

    @Lob
    @Column(name = "avatar")
    private byte[] avatar;

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

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public List<Workspace> getWorkspaces() {
        return workspaces;
    }

        public void setNickname(String value) throws IllegalArgumentException {
        ArgumentGuard.assertStringNotNullOrEmpty(value);
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
}