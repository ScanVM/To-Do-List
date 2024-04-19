package model;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    /**
     * Construtor com parâmetros.
     *
     * @param username Nome do usuário.
     * @param passwordHash Hash da senha do usuário.
     * @param email E-mail do usuário.
     * @param createdAt Data e hora da criação do usuário.
     */
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

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String formattedcreatedAt = createdAt.format(formatter);
        return "Usuário " + "\n" +
                "Username: " + username + "\n" +
                "E-mail: " + email + "\n" +
                "Data de criação da conta: " + formattedcreatedAt + "\n";
    }

}
