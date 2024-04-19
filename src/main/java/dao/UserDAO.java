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


public class UserDAO {
    private final MongoCollection<User> collection;
    JFrame frame = new JFrame();

    public UserDAO() {
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        MongoDatabase database = MongoDBConfig.getDatabase().withCodecRegistry(pojoCodecRegistry);
        collection = database.getCollection("Users", User.class);
    }

    public ObjectId create(User user) {
        frame.setAlwaysOnTop(true);
        if (isUsernameExists(user.getUsername())){
            JOptionPane.showMessageDialog(frame, "Usuário já existe. Crie outro username.");
            return null;
        }

        InsertOneResult result = collection.insertOne(user);
        return result.getInsertedId().asObjectId().getValue();
    }

    public boolean deleteUserById(ObjectId userId) {
        Document filter = new Document("_id", userId);
        DeleteResult result = collection.deleteOne(filter);
        return result.getDeletedCount() > 0;
    }

    public boolean isUsernameExists(String username) {
        Document filter = new Document("username", username);
        User existingUser = collection.find(filter).first();
        return existingUser != null;
    }
    public ObjectId authenticateUser(String username, String passwordHash) {
        Bson filter = new Document("username", username).append("passwordHash", passwordHash);
        User existingUser = collection.find(filter).first();

        if (existingUser != null){
            return existingUser.getId();
        }else {
            return null;
        }
    }
    public boolean updatePassword(ObjectId userId, String newPassword) {
        Bson filter = Filters.eq("_id", userId);
        Bson update = Updates.set("passwordHash", newPassword);
        UpdateResult result = collection.updateOne(filter, update);
        return result.getMatchedCount() > 0;
    }
    public boolean updateEmail(ObjectId userId, String newEmail) {
        Bson filter = Filters.eq("_id", userId);
        Bson update = Updates.set("email", newEmail);
        UpdateResult result = collection.updateOne(filter, update);
        return result.getMatchedCount() > 0;
    }
    public User getUserInfo(ObjectId userId) {
        Bson filter = Filters.eq("_id", userId);
        return collection.find(filter).first();
    }
}
