package dao;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import model.User;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import service.MongoDBConfig;

import javax.swing.*;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

/**
 * Classe UserDAO.
 * Esta classe é responsável por realizar operações de banco de dados relacionadas ao usuário.
 */
public class UserDAO {
    private final MongoCollection<User> collection;
    JFrame frame = new JFrame();

    /**
     * Construtor padrão.
     * Inicializa a coleção de usuários do banco de dados.
     */
    public UserDAO() {
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        MongoDatabase database = MongoDBConfig.getDatabase().withCodecRegistry(pojoCodecRegistry);
        collection = database.getCollection("Users", User.class);
    }

    /**
     * Cria um novo usuário no banco de dados.
     *
     * @param user O usuário a ser criado.
     * @return O ObjectId do usuário criado, ou null se o nome de usuário já existir.
     */
    public ObjectId create(User user) {
        frame.setAlwaysOnTop(true);
        if (isUsernameExists(user.getUsername())){
            JOptionPane.showMessageDialog(frame, "Usuário já existe. Crie outro username.");
            return null;
        }

        InsertOneResult result = collection.insertOne(user);
        return result.getInsertedId().asObjectId().getValue();
    }

    /**
     * Deleta um usuário do banco de dados pelo seu ID.
     *
     * @param userId O ObjectId do usuário a ser deletado.
     * @return true se o usuário foi deletado com sucesso, false caso contrário.
     */
    public boolean deleteUserById(ObjectId userId) {
        Document filter = new Document("_id", userId);
        DeleteResult result = collection.deleteOne(filter);
        return result.getDeletedCount() > 0;
    }

    /**
     * Verifica se um nome de usuário já existe no banco de dados.
     *
     * @param username O nome de usuário a ser verificado.
     * @return true se o nome de usuário já existir, false caso contrário.
     */
    public boolean isUsernameExists(String username) {
        Document filter = new Document("username", username);
        User existingUser = collection.find(filter).first();
        return existingUser != null;
    }

    /**
     * Autentica um usuário.
     *
     * @param username O nome de usuário.
     * @param passwordHash O hash da senha.
     * @return O ObjectId do usuário se a autenticação for bem sucedida, null caso contrário.
     */
    public ObjectId authenticateUser(String username, String passwordHash) {
        Bson filter = new Document("username", username).append("passwordHash", passwordHash);
        User existingUser = collection.find(filter).first();

        if (existingUser != null){
            return existingUser.getId();
        }else {
            return null;
        }
    }

    /**
     * Atualiza a senha de um usuário.
     *
     * @param userId O ObjectId do usuário.
     * @param newPassword A nova senha do usuário.
     * @return true se a senha foi atualizada com sucesso, false caso contrário.
     */
    public boolean updatePassword(ObjectId userId, String newPassword) {
        Bson filter = Filters.eq("_id", userId);
        Bson update = Updates.set("passwordHash", newPassword);
        UpdateResult result = collection.updateOne(filter, update);
        return result.getMatchedCount() > 0;
    }

    /**
     * Atualiza o e-mail de um usuário.
     *
     * @param userId O ObjectId do usuário.
     * @param newEmail O novo e-mail do usuário.
     * @return true se o e-mail foi atualizado com sucesso, false caso contrário.
     */
    public boolean updateEmail(ObjectId userId, String newEmail) {
        Bson filter = Filters.eq("_id", userId);
        Bson update = Updates.set("email", newEmail);
        UpdateResult result = collection.updateOne(filter, update);
        return result.getMatchedCount() > 0;
    }

    /**
     * Retorna as informações de um usuário.
     *
     * @param userId O ObjectId do usuário.
     * @return O usuário correspondente ao ObjectId fornecido.
     */
    public User getUserInfo(ObjectId userId) {
        Bson filter = Filters.eq("_id", userId);
        return collection.find(filter).first();
    }
}
