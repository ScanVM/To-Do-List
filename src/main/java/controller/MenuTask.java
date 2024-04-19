package controller;
import dao.TaskDAO;
import model.Task;
import org.bson.types.ObjectId;
import javax.swing.*;
import java.time.LocalDateTime;
import java.util.List;

public class MenuTask extends TaskDAO {

    /**
     * Exibe o menu de tarefas para o usuário.
     *
     * @param userId O ObjectId do usuário.
     * @return true se o menu foi exibido com sucesso, false caso contrário.
     */
    public boolean interfaceMenuTask(ObjectId userId){
        //Opções de menu para tarefas
        String[] menu = {"Criar tarefa", "Buscar tarefa","Editar tarefa", "Excluir tarefa", "Concluir tarefa", "Voltar"};
        List<Task> searchList; // Lista para armazenar as tarefas buscadas
        StringBuilder tasksString = new StringBuilder(); // StringBuilder para construir a string de tarefas
        JFrame frame = new JFrame();
        frame.setAlwaysOnTop(true);

        while (true) {
            int choice = JOptionPane.showOptionDialog(frame, "Selecione uma operação:", "Menu de tarefas", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, menu, menu[0]);
            switch (choice) {
                case 0: // Opção de Criar tarefa
                    try {
                        JTextField titleField = new JTextField();
                        JTextField descriptionField = new JTextField();
                        JTextField statusField = new JTextField();
                        JTextField priorityField = new JTextField();
                        JTextField tagField = new JTextField();
                        Object[] message = {
                                "Insira o titulo de sua tarefa:", titleField,
                                "Insira uma descrição da tarefa:", descriptionField,
                                "Coloque o seu status personalizado:", statusField,
                                "Informe o grau de prioridade entre 0 á 10:", priorityField,
                                "Insira uma tag para identificar sua tarefa:", tagField
                        };
                        // Exibe uma caixa de diálogo para o usuário inserir as informações da tarefa
                        int option = JOptionPane.showConfirmDialog(frame, message, "Criar Tarefa", JOptionPane.OK_CANCEL_OPTION);

                        if (option == JOptionPane.OK_OPTION) { // Se o usuário clicar em OK
                            String title = titleField.getText();
                            String description = descriptionField.getText();
                            String status = statusField.getText();
                            int priority = Integer.parseInt(priorityField.getText());
                            String tag = tagField.getText();

                            if (title.isEmpty() || description.isEmpty() || status.isEmpty() || tag.isEmpty()) {
                                JOptionPane.showMessageDialog(frame, "Título, descrição, status e tag não podem ser vazios.");
                                break;
                            }
                            // Cria uma nova tarefa
                            Task newTask = new Task(userId, title, description, status, priority, LocalDateTime.now(), null, tag);
                            if (createTask(newTask)){
                                JOptionPane.showMessageDialog(frame, "Tarefa cadastrada com sucesso!");
                            }
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(frame, "Erro: A prioridade deve ser um número entre 0 e 10.");
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(frame, "Ocorreu um erro ao criar a tarefa. Por favor, tente novamente.");
                        System.out.println(e.getMessage());
                    }
                    break;
                case 1:  // Opção de Buscar tarefa
                    try {
                        //Menu de opções para busca
                        String[] menuSearch = {"Buscar por título", "Buscar por tag","Buscar por prioridade", "Buscar todas as tarefas"};
                        int choiceSearch = JOptionPane.showOptionDialog(frame, "Selecione uma operação:", "Menu de pesquisa", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, menuSearch, menuSearch[0]);
                        switch (choiceSearch) {
                            case 0: // Opção de Buscar por título
                                String titleSearch = JOptionPane.showInputDialog(frame, "Insira o titulo para pesquisa: ");
                                searchList = searchTasks(userId, null, titleSearch, null);
                                for(Task task : searchList){
                                    tasksString.append(task.toString()).append("\n\n");
                                }
                                break;
                            case 1:  // Opção de Buscar por tag
                                String tagSearch = JOptionPane.showInputDialog(frame, "Insira a tag para pesquisa: ");
                                searchList = searchTasks(userId, tagSearch, null, null);
                                for(Task task : searchList){
                                    tasksString.append(task.toString()).append("\n\n");
                                }
                                break;
                            case 2: // Opção de Buscar por prioridade
                                String prioritySearch = JOptionPane.showInputDialog(frame, "Insira o grau de prioridade para pesquisa: ");
                                try {
                                    int priority = Integer.parseInt(prioritySearch); // Converte a prioridade para um número inteiro
                                    searchList = searchTasks(userId, null, null, priority);
                                    for(Task task : searchList){ // Para cada tarefa encontrada
                                        tasksString.append(task.toString()).append("\n\n"); // Adiciona a tarefa à string de tarefas
                                    }
                                } catch (NumberFormatException e) {
                                    JOptionPane.showMessageDialog(frame, "Por favor, insira um número válido para a prioridade.");
                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(frame, "Houve um problema ao buscar as tarefas. Por favor, tente novamente.");
                                }
                                break;
                            case 3: // Opção de Buscar todas as tarefas
                                searchList = searchTasks(userId, null, null, null);
                                for(Task task : searchList){
                                    tasksString.append(task.toString()).append("\n\n");
                                }
                                break;
                            default:
                                JOptionPane.showMessageDialog(frame, "Opção inválida !");
                                break;
                        }
                    }catch (Exception e) {
                        JOptionPane.showMessageDialog(frame, "Ocorreu um erro ao realizar a pesquisa. Tente novamente.");
                        System.out.println(e.getMessage());
                    }
                    //Exibe todas as tarefas encontradas de acordo com o filtro
                    JOptionPane.showMessageDialog(frame, tasksString.toString());
                    //Limpa o StringBuilder para a próxima pesquisa
                    tasksString.setLength(0);
                    break;
                case 2: // Opção de Editar tarefa
                    try {
                        String title = JOptionPane.showInputDialog(frame, "Insira o titulo de sua tarefa: ");
                        if (title == null || title.isEmpty()) {
                            JOptionPane.showMessageDialog(frame, "O título não pode ser vazio.");
                            break;
                        }
                        ObjectId taskIdUpdate = findTaskIdByTitle(userId, title);  // Encontra o ID da tarefa pelo título
                        if (taskIdUpdate != null) {
                            // Opções do menu de edição
                            String[] menuUpdate = {"Editar titulo", "Editar descrição","Editar os dois"};
                            int choiceUpdate = JOptionPane.showOptionDialog(frame, "Selecione uma operação:", "Menu de edição", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, menuUpdate, menuUpdate[0]);
                            switch (choiceUpdate) {
                                case 0: // Opção de Editar titulo
                                    String newTitle = JOptionPane.showInputDialog(frame, "Insira um novo titulo para sua tarefa: ");
                                    if (newTitle == null || newTitle.isEmpty()) {
                                        JOptionPane.showMessageDialog(frame, "O novo título não pode ser vazio.");
                                        break;
                                    }
                                    if (updateTask(taskIdUpdate, userId, newTitle, null)){
                                        JOptionPane.showMessageDialog(frame, "Título atualizado com sucesso!");
                                    }
                                    break;
                                case 1:  // Opção de Editar descrição
                                    String newDescription = JOptionPane.showInputDialog(frame, "Insira uma nova descrição para sua tarefa: ");
                                    if (newDescription == null || newDescription.isEmpty()) {
                                        JOptionPane.showMessageDialog(frame, "A nova descrição não pode ser vazia.");
                                        break;
                                    }
                                    if (updateTask(taskIdUpdate, userId, null, newDescription)){
                                        JOptionPane.showMessageDialog(frame, "Descrição atualizada com sucesso!");
                                    }
                                    break;
                                case 2: // Opção de Editar os dois
                                    JTextField newTitleField = new JTextField();
                                    JTextField newDescriptionField = new JTextField();
                                    Object[] message = {
                                            "Insira um novo titulo para sua tarefa:", newTitleField,
                                            "Insira uma nova descrição para sua tarefa:", newDescriptionField
                                    };

                                    int option = JOptionPane.showConfirmDialog(frame, message, "Editar Tarefa", JOptionPane.OK_CANCEL_OPTION);
                                    if (option == JOptionPane.OK_OPTION) {
                                        String newTitleOption2 = newTitleField.getText();
                                        String newDescriptionOption2 = newDescriptionField.getText();

                                        if (newTitleOption2.isEmpty() || newDescriptionOption2.isEmpty()) {
                                            JOptionPane.showMessageDialog(frame, "O novo título e a nova descrição não podem ser vazios.");
                                            break;
                                        }

                                        if (updateTask(taskIdUpdate, userId, newTitleOption2, newDescriptionOption2)){
                                            JOptionPane.showMessageDialog(frame, "Título e descrição atualizados com sucesso!");
                                        }
                                    }
                                    break;
                                default:
                                    JOptionPane.showMessageDialog(frame, "Opção inválida.");
                                    break;
                            }
                        } else {
                            JOptionPane.showMessageDialog(frame, "Tarefa não encontrada.");
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(frame, "Houve um problema ao atualizar a tarefa. Por favor, tente novamente.");
                    }
                    break;
                case 3: //Opção de remoção de tarefa
                    try {
                        String title = JOptionPane.showInputDialog(frame, "Insira o titulo de sua tarefa: ");
                        if (title == null || title.isEmpty()) {
                            JOptionPane.showMessageDialog(frame, "O título não pode ser vazio.");
                            break;
                        }
                        //Autentica a tarefa
                        ObjectId taskId = findTaskIdByTitle(userId, title);
                        if (taskId != null) {
                            //Retira a tarefa encontrada
                            if (deleteTaskById(taskId, userId)) {
                                JOptionPane.showMessageDialog(frame, "Tarefa removida com sucesso!");
                            } else {
                                JOptionPane.showMessageDialog(frame, "Houve um problema para remover sua tarefa.");
                            }
                        } else {
                            JOptionPane.showMessageDialog(frame, "Tarefa não encontrada.");
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(frame, "Houve um problema ao remover a tarefa. Por favor, tente novamente.");
                    }
                    break;
                case 4: // Opção para concluir tarefa
                    try {
                        String titleCompleted = JOptionPane.showInputDialog(frame, "Insira o titulo de sua tarefa: ");
                        if (titleCompleted == null || titleCompleted.isEmpty()) {
                            JOptionPane.showMessageDialog(frame, "O título não pode ser vazio.");
                            break;
                        }
                        //Autentica a tarefa
                        ObjectId taskIdCompleted = findTaskIdByTitle(userId, titleCompleted);
                        if (taskIdCompleted != null) {
                            //Conclui a tarefa selecionada
                            if (completeTask(taskIdCompleted, userId)) {
                                JOptionPane.showMessageDialog(frame, "Tarefa concluída com sucesso!");
                            } else {
                                JOptionPane.showMessageDialog(frame, "Houve um problema para concluir sua tarefa.");
                            }
                        } else {
                            JOptionPane.showMessageDialog(frame, "Tarefa não encontrada.");
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(frame, "Houve um problema ao concluir a tarefa. Por favor, tente novamente.");
                    }
                    break;
                case 5: // Opção que retorna ao menu anterior
                    System.out.println("Retornando ao menu de usuário...");
                    return true;
                default:
                    JOptionPane.showMessageDialog(frame, "Escolha uma opção válida do menu.");
                    break;
            }
        }
    }
}
