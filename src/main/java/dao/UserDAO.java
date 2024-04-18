package dao;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import model.User;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;


public class UserDAO {
    private final MongoCollection<User> collection;

    public UserDAO() {
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        MongoDatabase database = MongoDBConfig.getDatabase().withCodecRegistry(pojoCodecRegistry);
        collection = database.getCollection("Users", User.class);
    }

    public ObjectId create(User user) {
        if (isUsernameExists(user.getUserame())){
            System.out.print("Usuário já existe. Crie outro username.");
            return null;
        }

        InsertOneResult result = collection.insertOne(user);
        return result.getInsertedId().asObjectId().getValue();
    }

    public boolean deleteUserByUsername(String username) {
        Document filter = new Document("username", username);
        DeleteResult result = collection.deleteOne(filter);
        return result.getDeletedCount() > 0;
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
        Document filter = new Document("username", username).append("passwordHash", passwordHash);
        User existingUser = collection.find(filter).first();

        if (existingUser != null){
            return existingUser.getId();
        }else {
            return null;
        }
    }
}
