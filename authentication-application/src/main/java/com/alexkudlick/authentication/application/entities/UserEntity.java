package com.alexkudlick.authentication.application.entities;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;
import java.util.function.Consumer;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "username", nullable = false, unique = true)
    private String userName;

    @Column(name = "encrypted_password", nullable = false, length = 1024)
    private String encryptedPassword;

    private UserEntity() {

    }

    public static void withUnsavedInstance(String userName, String encryptedPassword, Consumer<UserEntity> consumer) {
        UserEntity entity = new UserEntity();
        entity.userName = Objects.requireNonNull(userName);
        entity.encryptedPassword = Objects.requireNonNull(encryptedPassword);
        consumer.accept(entity);
    }

    public long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEntity)) return false;
        UserEntity that = (UserEntity) o;
        return id == that.id &&
            Objects.equals(userName, that.userName) &&
            Objects.equals(encryptedPassword, that.encryptedPassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userName, encryptedPassword);
    }

    @Override
    public String toString() {
        return "UserEntity{" +
            "id=" + id +
            ", userName='" + userName + '\'' +
            ", encryptedPassword='" + encryptedPassword + '\'' +
            '}';
    }
}
