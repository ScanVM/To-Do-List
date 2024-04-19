package model;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

public class User {
    @BsonId
    private ObjectId id;
    @BsonProperty("username")
    private String username;
    @BsonProperty("passwordHash")
    private String passwordHash;
    @BsonProperty("email")
    private String email;
    //Hora e dia da criação do user
    @BsonProperty("createdAt")
    private LocalDateTime createdAt;

    public User(){}

    public User(String username, String passwordHash, String email, LocalDateTime createdAt) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.createdAt = createdAt;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String userame) {
        this.username = userame;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
