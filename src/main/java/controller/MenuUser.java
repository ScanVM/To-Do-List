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
                        } else {
                            JOptionPane.showMessageDialog(frame, "Falha no login. Por favor, verifique seu nome de usuário e senha.");
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(frame, "Houve um problema ao fazer login. Por favor, tente novamente.");
                    }
                    break;
                case 1:
                    try {
                        JTextField usernameField = new JTextField();
                        JPasswordField passwordField = new JPasswordField();
                        JTextField emailField = new JTextField();
                        Object[] message = {
                                "Insira um username:", usernameField,
                                "Insira uma senha:", passwordField,
                                "Insira seu e-mail:", emailField
                        };

                        int option = JOptionPane.showConfirmDialog(frame, message, "Cadastro", JOptionPane.OK_CANCEL_OPTION);
                        if (option == JOptionPane.OK_OPTION) {
                            String username = usernameField.getText();
                            String password = new String(passwordField.getPassword());
                            String email = emailField.getText();

                            if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                                JOptionPane.showMessageDialog(frame, "Username, senha e e-mail não podem ser vazios.");
                                break;
                            }

                            User newUser = new User(username, password, email, LocalDateTime.now());
                            ObjectId newUserId = create(newUser);
                            if (newUserId != null) {
                                JOptionPane.showMessageDialog(frame, "Usuário cadastrado com sucesso.");
                                boolean returnToUserMenu = menuTask.interfaceMenuTask(newUserId);
                                if (returnToUserMenu) {
                                    break;
                                }
                            } else {
                                JOptionPane.showMessageDialog(frame, "Houve um problema ao criar o usuário.");
                            }
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(frame, "Houve um problema ao criar o usuário. Por favor, tente novamente.");
                    }
                    break;
                case 2:
                    try {
                        ObjectId userIdentifier = login();
                        if (userIdentifier != null) {
                            String choiceUser = JOptionPane.showInputDialog(frame, "Tem certeza que deseja excluir sua conta ? Y/N").toLowerCase();
                            if (choiceUser == null || choiceUser.isEmpty() || (!choiceUser.equals("y") && !choiceUser.equals("n"))) {
                                JOptionPane.showMessageDialog(frame, "Por favor, insira 'y' para confirmar ou 'n' para cancelar.");
                                break;
                            }
                            if (choiceUser.equals("y")) {
                                try {
                                    if (deleteUserById(userIdentifier)) {
                                        JOptionPane.showMessageDialog(frame, "Excluído com sucesso!");
                                    } else {
                                        JOptionPane.showMessageDialog(frame, "Houve um problema para remover sua conta.");
                                    }
                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(frame, "Houve um erro ao excluir sua conta. Por favor, tente novamente.");
                                }
                            }
                        } else {
                            JOptionPane.showMessageDialog(frame, "Falha no login. Por favor, verifique seu nome de usuário e senha.");
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(frame, "Houve um problema ao fazer login. Por favor, tente novamente.");
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
        try {
            JTextField usernameField = new JTextField();
            JPasswordField passwordField = new JPasswordField();
            Object[] message = {
                    "Insira seu username:", usernameField,
                    "Insira sua senha:", passwordField
            };

            int option = JOptionPane.showConfirmDialog(frame, message, "Login", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (username.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "O nome de usuário não pode ser vazio.");
                    return null;
                }
                if (password.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "A senha não pode ser vazia.");
                    return null;
                }
                return authenticateUser(username, password);
            } else {
                return null;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Houve um problema ao fazer login. Por favor, tente novamente.");
            return null;
        }
    }
}
