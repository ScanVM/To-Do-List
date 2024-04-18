package model;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

public class Task {
    @BsonProperty("_id")
    private ObjectId id;
    @BsonProperty("user_id")
    private ObjectId userId;
    @BsonProperty("title")
    private String title;
    @BsonProperty("description")
    private String description;
    @BsonProperty("status")
    private String status;
    @BsonProperty("priority")
    private int priority = 0;
    @BsonProperty("completed")
    private boolean completed = false;
    @BsonProperty("date_created")
    private LocalDateTime dateCreated;
    @BsonProperty("dateCompleted")
    private LocalDateTime dateCompleted;
    @BsonProperty("tag")
    private String tag;

    public Task(){}

    public Task(ObjectId userId, String title, String description, String status, int priority, LocalDateTime dateCreated, LocalDateTime dateCompleted, String tag) {
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.dateCreated = dateCreated;
        this.dateCompleted = dateCompleted;
        this.tag = tag;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalDateTime getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(LocalDateTime dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", priority=" + priority +
                ", completed=" + completed +
                ", dateCreated=" + dateCreated +
                ", dateCompleted=" + dateCompleted +
                ", tag='" + tag + '\'' +
                '}';
    }
}
