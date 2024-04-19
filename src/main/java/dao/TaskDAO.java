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

/**
 * Classe TaskDAO.
 * Esta classe é responsável por realizar operações de banco de dados relacionadas à tarefa.
 */
public class TaskDAO {
    private final MongoCollection<Task> collection;

    /**
     * Construtor padrão.
     * Inicializa a coleção de tarefas do banco de dados.
     */
    public TaskDAO() {
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        MongoDatabase database = MongoDBConfig.getDatabase().withCodecRegistry(pojoCodecRegistry);
        collection = database.getCollection("Tasks", Task.class);
    }

    /**
     * Cria uma nova tarefa no banco de dados.
     *
     * @param task A tarefa a ser criada.
     * @return true se a tarefa foi criada com sucesso, false caso contrário.
     */
    public boolean createTask(Task task) {
        if (isTaskExist(task.getUserId(), task.getTitle())){
            throw new IllegalArgumentException("A tarefa já existe.");
        }
        collection.insertOne(task);
        return true;
    }

    /**
     * Atualiza uma tarefa no banco de dados.
     *
     * @param taskId O ObjectId da tarefa a ser atualizada.
     * @param userId O ObjectId do usuário que criou a tarefa.
     * @param newTitle O novo título da tarefa.
     * @param newDescription A nova descrição da tarefa.
     * @return true se a tarefa foi atualizada com sucesso, false caso contrário.
     */
    public boolean updateTask(ObjectId taskId, ObjectId userId, String newTitle, String newDescription) {
        Bson filter =  Filters.and(Filters.eq("_id",taskId), Filters.eq("user_id", userId));
        Document update = new Document();
        if (newTitle != null && !newTitle.isEmpty()){
            update.append("title", newTitle);
        }
        if (newDescription != null && !newDescription.isEmpty()){
            update.append("description", newDescription);
        }
        UpdateResult result = collection.updateOne(filter, new Document("$set", update));
        return result.getMatchedCount() > 0;
    }

    /**
     * Deleta uma tarefa do banco de dados pelo seu ID.
     *
     * @param taskId O ObjectId da tarefa a ser deletada.
     * @param userId O ObjectId do usuário que criou a tarefa.
     * @return true se a tarefa foi deletada com sucesso, false caso contrário.
     */
    public boolean deleteTaskById(ObjectId taskId, ObjectId userId) {
        Bson filter = Filters.and(Filters.eq("_id", taskId), Filters.eq("user_id", userId));
        DeleteResult result = collection.deleteOne(filter);
        if (result.getDeletedCount() == 0){
            throw  new IllegalArgumentException("Tarefa não encontrada.");
        }
        return true;
    }

    /**
     * Busca tarefas no banco de dados.
     *
     * @param userId O ObjectId do usuário que criou as tarefas.
     * @param tag A tag das tarefas a serem buscadas.
     * @param title O título das tarefas a serem buscadas.
     * @param priority A prioridade das tarefas a serem buscadas.
     * @return Uma lista de tarefas que correspondem aos critérios de busca.
     */
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

    /**
     * Verifica se uma tarefa já existe no banco de dados.
     *
     * @param userId O ObjectId do usuário que criou a tarefa.
     * @param title O título da tarefa.
     * @return true se a tarefa já existir, false caso contrário.
     */
    private boolean isTaskExist(ObjectId userId, String title) {
        Bson filter =  Filters.and(Filters.eq("user_id", userId), Filters.eq("title", title));
        long count = collection.countDocuments(filter);
        return count > 0;
    }

    /**
     * Encontra o ObjectId de uma tarefa pelo seu título.
     *
     * @param userId O ObjectId do usuário que criou a tarefa.
     * @param title O título da tarefa.
     * @return O ObjectId da tarefa, ou null se a tarefa não for encontrada.
     */
    public ObjectId findTaskIdByTitle(ObjectId  userId, String title) {
        Bson filter =  Filters.and(Filters.eq("user_id", userId), Filters.eq("title",title));

        Task task = collection.find(filter).first();

        if (task != null){
            return task.getId();
        }else {
            return null;
        }
    }

    /**
     * Completa uma tarefa no banco de dados.
     *
     * @param taskId O ObjectId da tarefa a ser completada.
     * @param userId O ObjectId do usuário que criou a tarefa.
     * @return true se a tarefa foi completada com sucesso, false caso contrário.
     */
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
