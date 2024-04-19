package controller;
import dao.UserDAO;
import model.User;
import org.bson.types.ObjectId;
import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import java.time.LocalDateTime;

/**
 * Classe que possui o menu com opções para ações do usuário
 */
public class MenuUser extends UserDAO {
    MenuTask menuTask = new MenuTask();
    JFrame frame = new JFrame();

    /**
     * Interface do menu visivel ao usuário
     */
    public void interfaceMenuUser(){
        String[] menu = {"Login", "Cadastro","Remover usuário","Sair"}; // Opções do menu principal
        frame.setAlwaysOnTop(true); // Define que a janela sempre aparecerá no topo das outras
        while (true) {
            int choice = JOptionPane.showOptionDialog(frame, "Selecione uma operação:", "Menu", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, menu, menu[0]);
            //Menu inicial para o usuário entrar no sistema
            switch (choice) {
                case 0:
                    try {
                        ObjectId userIdentifier = login(); //Autenticação do usuário por meio do login
                        if (userIdentifier != null) {
                            //Menu de funções sobre o usuário
                            String[] menuOthersUser= {"Menu de tarefas","Alterar senha", "Alterar e-mail","Informações sobre a conta","Voltar"};
                           boolean continueLoop = true;
                           do {
                               int choiceOthers = JOptionPane.showOptionDialog(frame, "Selecione uma operação:", "Menu usuário", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, menuOthersUser, menuOthersUser[0]);
                               switch (choiceOthers) {
                                   case 0: // Abre o menu de tarefas
                                       boolean returnToUserMenu = menuTask.interfaceMenuTask(userIdentifier);
                                       if (returnToUserMenu) {
                                           break;
                                       }
                                       break;
                                   case 1: // Alterar a senha
                                       JPasswordField newPasswordField = new JPasswordField();
                                       Object[] message = {
                                               "Insira sua nova senha:", newPasswordField
                                       };

                                       int option = JOptionPane.showConfirmDialog(null, message, "Senha", JOptionPane.OK_CANCEL_OPTION);
                                       if (option == JOptionPane.OK_OPTION) {
                                           String newPassword = new String(newPasswordField.getPassword());

                                           if (newPassword.isEmpty()) {
                                               JOptionPane.showMessageDialog(null, "A senha não pode ser vazia.");
                                           } else {
                                               if (updatePassword(userIdentifier, newPassword)) {
                                                   JOptionPane.showMessageDialog(frame, "Senha trocada com sucesso!");
                                               } else {
                                                   JOptionPane.showMessageDialog(frame, "Houve um problema para trocar sua senha, tente novamente..");
                                               }
                                           }
                                       }
                                       break;
                                   case 2: //Alterar e-mail
                                       String newEmail = JOptionPane.showInputDialog(frame, "Insira o seu novo endereço de e-mail:");
                                       if (newEmail == null || newEmail.isEmpty()) {
                                           JOptionPane.showMessageDialog(frame, "O novo e-mail não pode ser vazio.");
                                       } else {
                                           if (updateEmail(userIdentifier, newEmail)) {
                                               JOptionPane.showMessageDialog(frame, "E-mail trocado com sucesso !");
                                           } else {
                                               JOptionPane.showMessageDialog(frame, "Houve um problema para trocar seu e-mail, tente novamente.");
                                           }
                                       }
                                       break;
                                   case 3: //Informações sobre a conta do usuário
                                       User user;
                                       user = getUserInfo(userIdentifier);
                                       if (user != null) {
                                           JOptionPane.showMessageDialog(frame, user.toString());
                                       } else {
                                           JOptionPane.showMessageDialog(frame, "Não foi possível exibir os dados, tente novamente.");
                                       }
                                       break;
                                   case 4: // Opção para retornar ao menu anterior
                                       continueLoop = false;
                                       break;
                                   default:
                                       JOptionPane.showMessageDialog(frame, "Escolha uma opção válida do menu.");
                                       break;
                               }
                           }while (continueLoop);
                        } else {
                            JOptionPane.showMessageDialog(frame, "Falha no login. Por favor, verifique seu nome de usuário e senha.");
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(frame, "Houve um problema ao fazer login. Por favor, tente novamente.");
                    }
                    break;
                case 1: // Opção para cadastro
                    try {
                        JTextField usernameField = new JTextField();
                        JPasswordField passwordField = new JPasswordField();
                        JTextField emailField = new JTextField();
                        Object[] message = {
                                "Insira um username:", usernameField,
                                "Insira uma senha:", passwordField,
                                "Insira seu e-mail:", emailField
                        };
                        //Recolho as informações do usuário para criar sua conta
                        int option = JOptionPane.showConfirmDialog(frame, message, "Cadastro", JOptionPane.OK_CANCEL_OPTION);
                        if (option == JOptionPane.OK_OPTION) {
                            String username = usernameField.getText();
                            String password = new String(passwordField.getPassword());
                            String email = emailField.getText();

                            if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                                JOptionPane.showMessageDialog(frame, "Username, senha e e-mail não podem ser vazios.");
                                break;
                            }
                            //Criptografia da senha
                            String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());

                            //Crio o objeto user
                            User newUser = new User(username, passwordHash, email, LocalDateTime.now());

                            //Envio para o banco o novo usuário
                            ObjectId newUserId = create(newUser);
                            if (newUserId != null) {
                                JOptionPane.showMessageDialog(frame, "Usuário registrado com sucesso.");
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
                case 2: //Remove o usuário
                    try {
                        //Autentica o usuário para encontra-lo no banco de dados
                        ObjectId userIdentifier = login();
                        if (userIdentifier != null) {
                            //Confirma se ele de fato quer excluir
                            String choiceUser = JOptionPane.showInputDialog(frame, "Tem certeza que deseja excluir sua conta ? Y/N").toLowerCase();
                            if (choiceUser.isEmpty() || !choiceUser.equals("y") && !choiceUser.equals("n")) {
                                JOptionPane.showMessageDialog(frame, "Por favor, insira 'y' para confirmar ou 'n' para cancelar.");
                                break;
                            }
                            if (choiceUser.equals("y")) {
                                try {
                                    //Exclui o usuário
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
                case 3: //Fecha o programa
                    System.out.println("Fechando sua To-Do-List...");
                    System.exit(0);
                    break;
                default:
                    JOptionPane.showMessageDialog(frame, "Escolha uma opção válida do menu.");
                    break;
            }
        }
    }

    /**
     * Realiza o login de um usuário.
     *
     * @return O ObjectId do usuário se o login for bem sucedido, null caso contrário.
     */
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
                // Recebe a senha em criptografada do banco
                String passwordHashConfirm = getPasswordHash(username);
                System.out.println(passwordHashConfirm);
                //Valida se a senha digitada é igual a senha do banco
                if (passwordHashConfirm != null && BCrypt.checkpw(password,passwordHashConfirm)){
                    System.out.println("Vou lá");
                    //Autentica o usuário nesse método
                    return authenticateUser(username, passwordHashConfirm);
                }
                return null;
            } else {
                return null;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Houve um problema ao fazer login. Por favor, tente novamente.");
            return null;
        }
    }
}
