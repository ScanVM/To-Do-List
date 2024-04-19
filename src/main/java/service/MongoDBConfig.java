package service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import javax.swing.*;

/**
 * Classe de configuração do MongoDB.
 * Esta classe é responsável por estabelecer a conexão com o banco de dados MongoDB.
 */
public class MongoDBConfig {
    private static final String CONNECTION_STRING = "mongodb+srv://viniciusscandura:ItgShkVQVHdgLvi4@cluster0.anpxsj6.mongodb.net/";
    private static final String DATABASE_NAME = "FInalProject";

    /**
     * Retorna uma instância do banco de dados especificado.
     * Cria uma conexão com o banco de dados MongoDB usando a string de conexão e retorna a instância do banco de dados.
     *
     * @return Uma instância do banco de dados ou null se a conexão falhar.
     */
    public static MongoDatabase getDatabase() {
        JFrame frame = new JFrame();
        frame.setAlwaysOnTop(true);
        try {
            MongoClient mongoClient = MongoClients.create(CONNECTION_STRING);
            return mongoClient.getDatabase(DATABASE_NAME);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Erro ao conectar ao banco de dados.");
            System.out.println(e.getMessage());
            return null;
        }
    }
}
