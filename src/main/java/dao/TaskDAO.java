package dao;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import model.Task;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import service.MongoDBConfig;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class TaskDAO {
    private final MongoCollection<Task> collection;

    public TaskDAO() {
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        MongoDatabase database = MongoDBConfig.getDatabase().withCodecRegistry(pojoCodecRegistry);
        collection = database.getCollection("Tasks", Task.class);
    }

    public boolean createTask(Task task) {
        if (isTaskExist(task.getUserId(), task.getTitle())){
            throw new IllegalArgumentException("A tarefa já existe.");
        }
        collection.insertOne(task);
        return true;
    }

    public boolean updateTask(ObjectId taskId, ObjectId userId, String newTitle, String newDescription) {
        Bson filter =  Filters.and(Filters.eq("_id",taskId), Filters.eq("user_id", userId));
        Document update = new Document();
        if (newTitle != null){
            update.append("$set", new Document("title",newTitle));
        }
        if (newDescription != null){
            update.append("$set", new Document("description",newDescription));
        }
        UpdateResult result = collection.updateOne(filter, update);
        if (result.getMatchedCount() == 0){
            throw  new IllegalArgumentException("Tarefa não encontrada.");
        }
        return true;
    }

    public boolean deleteTaskById(ObjectId taskId, ObjectId userId) {
        Bson filter = Filters.and(Filters.eq("_id", taskId), Filters.eq("user_id", userId));
        DeleteResult result = collection.deleteOne(filter);
        if (result.getDeletedCount() == 0){
            throw  new IllegalArgumentException("Tarefa não encontrada.");
        }
        return true;
    }

    public List<Task> searchTasks(ObjectId userId, String tag, String title, Integer priority) {
        List<Task> tasks = new ArrayList<>();
        List<Bson> filters = new ArrayList<>();
        filters.add(Filters.eq("user_id", userId));
        if (tag != null){
            filters.add(Filters.eq("tag", tag));
        }
        if (title != null){
            filters.add(Filters.eq("title", title));
        }
        if (priority != null){
            filters.add(Filters.eq("priority", priority));
        }

        Bson filter = Filters.and(filters);

        if (tag == null && title == null && priority == null){
            collection.find(filter).sort(Sorts.descending("priority")).into(tasks);
        }else{
            collection.find(filter).into(tasks);
        }
        return tasks;
    }

    private boolean isTaskExist(ObjectId userId, String title) {
        Bson filter =  Filters.and(Filters.eq("user_id", userId), Filters.eq("title", title));
        long count = collection.countDocuments(filter);
        return count > 0;
    }
    public ObjectId findTaskIdByTitle(ObjectId  userId, String title) {
        Bson filter =  Filters.and(Filters.eq("user_id", userId), Filters.eq("title",title));

        Task task = collection.find(filter).first();

        if (task != null){
            return task.getId();
        }else {
            return null;
        }
    }
    public boolean completeTask(ObjectId taskId, ObjectId userId) {
        Bson filter =  Filters.and(Filters.eq("_id", taskId), Filters.eq("user_id", userId));
        Bson update = Updates.combine(
                Updates.set("completed", true),
                Updates.set("status", "Concluída."),
                Updates.set("dateCompleted", LocalDateTime.now())
                );

        UpdateResult result = collection.updateOne(filter, update);
        if (result.getMatchedCount() == 0){
            throw  new IllegalArgumentException("Tarefa não encontrada.");
        }
        return true;
    }
}
