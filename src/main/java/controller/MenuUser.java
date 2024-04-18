package controller;
import dao.UserDAO;
import model.User;
import org.bson.types.ObjectId;
import javax.swing.*;
import java.time.LocalDateTime;

public class MenuUser extends UserDAO {
    MenuTask menuTask = new MenuTask();
    JFrame frame = new JFrame();

    public void interfaceMenuUser(){
        String[] menu = {"Login", "Cadastro","Remover usuário","Sair"};
        frame.setAlwaysOnTop(true);
        while (true) {
            int choice = JOptionPane.showOptionDialog(frame, "Selecione uma operação:", "Menu", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, menu, menu[0]);

            switch (choice) {
                case 0:
                    try {
                        ObjectId userIdentifier = login();
                        if (userIdentifier != null) {
                           boolean returnToUserMenu = menuTask.interfaceMenuTask(userIdentifier);
                           if (returnToUserMenu) {
                               break;
                           }
                        }

                    }catch (Exception e) {
                        System.out.println(e.getMessage());
                        //JOptionPane.showMessageDialog(null, e);
                    }
                    break;
                case 1:
                    try {
                        String username = JOptionPane.showInputDialog(frame, "Insira um username: ");
                        String password = JOptionPane.showInputDialog(frame, "Insira uma senha: ");
                        String email = JOptionPane.showInputDialog(frame, "Insira seu e-mail: ");
                        User newUser = new User(username,password,email, LocalDateTime.now());
                        ObjectId newUserId = create(newUser);
                        boolean returnToUserMenu = menuTask.interfaceMenuTask(newUserId);
                        if (returnToUserMenu) {
                            break;
                        }
                    }catch (Exception e) {
                        System.out.println(e.getMessage());
                        //JOptionPane.showMessageDialog(null, e);
                    }
                    break;
                case 2:
                    try {
                        ObjectId userIdentifier = login();
                        if (userIdentifier != null) {
                            String choiceUser = JOptionPane.showInputDialog(frame, "Tem certeza que deseja excluir sua conta ? Y/N").toLowerCase();
                            if (choiceUser.equals("y")) {
                                try {
                                    if (deleteUserById(userIdentifier)) {
                                        JOptionPane.showMessageDialog(frame, "Excluido com sucesso !");
                                    }
                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(frame, "Houve um erro ao excluir sua conta !");
                                }
                            }
                        }
                    }catch (Exception e) {
                        JOptionPane.showMessageDialog(frame, e);
                    }
                    break;
                case 3:
                    System.out.println("Fechando sua To-Do-List...");
                    System.exit(0);
                    break;
                default:
                    JOptionPane.showMessageDialog(frame, "Escolha uma opção válida do menu.");
                    break;
            }
        }
    }
    public ObjectId login() {
        frame.setAlwaysOnTop(true);
        JPasswordField passwordField = new JPasswordField();
        String username = JOptionPane.showInputDialog(frame, "Insira seu username: ");
        JOptionPane.showConfirmDialog(frame, passwordField, "Insira sua senha: ",JOptionPane.OK_CANCEL_OPTION);
        String password = new String(passwordField.getPassword());
        return authenticateUser(username, password);
    }
}
