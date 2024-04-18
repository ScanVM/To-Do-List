package controller;

import dao.UserDAO;
import model.User;
import org.bson.types.ObjectId;

import javax.swing.*;
import java.time.LocalDateTime;

public class MenuUser extends UserDAO {
    MenuTask menuTask = new MenuTask();

    public void interfaceMenuUser(){
        String[] menu = {"Login", "Cadastro","Remover usuário","Sair"};
        while (true) {
            int choice = JOptionPane.showOptionDialog(null, "Selecione uma operação:", "Menu", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, menu, menu[0]);

            switch (choice) {
                case 0:
                    try {
                        ObjectId userIdentifier = login();
                        if (userIdentifier != null) {
                            menuTask.interfaceMenuTask(userIdentifier);
                        }

                    }catch (Exception e) {
                        System.out.println(e.getMessage());
                        //JOptionPane.showMessageDialog(null, e);
                    }
                    break;
                case 1:
                    try {
                        String username = JOptionPane.showInputDialog(null, "Insira um username: ");
                        String password = JOptionPane.showInputDialog(null, "Insira uma senha: ");
                        String email = JOptionPane.showInputDialog(null, "Insira seu e-mail: ");
                        User newUser = new User(username,password,email, LocalDateTime.now());
                        ObjectId newUserId = create(newUser);
                        menuTask.interfaceMenuTask(newUserId);
                    }catch (Exception e) {
                        System.out.println(e.getMessage());
                        //JOptionPane.showMessageDialog(null, e);
                    }
                    break;
                case 2:
                    try {
                        ObjectId userIdentifier = login();
                        if (userIdentifier != null) {
                            String choiceUser = JOptionPane.showInputDialog(null, "Tem certeza que deseja excluir sua conta ? Y/N").toLowerCase();
                            if (choiceUser.equals("y")) {
                                try {
                                    if (deleteUserById(userIdentifier)) {
                                        JOptionPane.showMessageDialog(null, "Excluido com sucesso !");
                                    }
                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(null, "Houve um erro ao excluir sua conta !");
                                }
                            }
                        }
                    }catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e);
                    }
                    break;
                case 3:
                    System.out.println("Fechando sua To-Do-List...");
                    System.exit(0);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Escolha uma opção válida do menu.");
                    break;
            }
        }
    }
    public ObjectId login() {
        String username = JOptionPane.showInputDialog(null, "Insira seu username: ");
        String password = JOptionPane.showInputDialog(null, "Insira sua senha: ");
        return authenticateUser(username, password);
    }
}
