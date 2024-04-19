package controller;
import dao.TaskDAO;
import model.Task;
import org.bson.types.ObjectId;
import javax.swing.*;
import java.time.LocalDateTime;
import java.util.List;

public class MenuTask extends TaskDAO {
    public boolean interfaceMenuTask(ObjectId userId){
        String[] menu = {"Criar tarefa", "Buscar tarefa","Editar tarefa", "Excluir tarefa", "Concluir tarefa", "Sair"};
        List<Task> searchList;
        JFrame frame = new JFrame();
        frame.setAlwaysOnTop(true);

        while (true) {
            int choice = JOptionPane.showOptionDialog(frame, "Selecione uma operação:", "Menu de tarefas", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, menu, menu[0]);
            switch (choice) {
                case 0:
                    try {
                        String title = JOptionPane.showInputDialog(frame, "Insira o titulo de sua tarefa: ");
                        String description = JOptionPane.showInputDialog(frame, "Insira uma descrição da tarefa: ");
                        String status = JOptionPane.showInputDialog(frame, "Coloque o seu status personalizado: ");
                        int priority = Integer.parseInt(JOptionPane.showInputDialog(frame, "Informe o grau de prioridade entre 0 á 10: "));
                        String tag = JOptionPane.showInputDialog(frame, "Insira uma tag para identificar sua tarefa: ");
                        Task newTask = new Task(userId, title, description, status, priority, LocalDateTime.now(),null, tag);
                        if (createTask(newTask)){
                            JOptionPane.showMessageDialog(frame, "Tarefa cadastrada com sucesso!");
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(frame, "Erro: A prioridade deve ser um número entre 0 e 10.");
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(frame, "Ocorreu um erro ao criar a tarefa. Por favor, tente novamente.");
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    try {
                        String[] menuSearch = {"Buscar por título", "Buscar por tag","Buscar por prioridade", "Buscar todas as tarefas"};
                        int choiceSearch = JOptionPane.showOptionDialog(frame, "Selecione uma operação:", "Menu de pesquisa", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, menuSearch, menuSearch[0]);
                        switch (choiceSearch) {
                            case 0:
                                String titleSearch = JOptionPane.showInputDialog(frame, "Insira o titulo para pesquisa: ");
                                searchList = searchTasks(userId, null, titleSearch, null);
                                for(Task task : searchList){
                                    //JOptionPane.showMessageDialog(null, task.toString());
                                    System.out.println(task.toString());
                                }
                                break;
                            case 1:
                                String tagSearch = JOptionPane.showInputDialog(frame, "Insira a tag para pesquisa: ");
                                searchList = searchTasks(userId, tagSearch, null, null);
                                for(Task task : searchList){
                                    //JOptionPane.showMessageDialog(null, task.toString());
                                    System.out.println(task.toString());
                                }
                                break;
                            case 2:
                                String prioritySearch = JOptionPane.showInputDialog(frame, "Insira o grau de prioridade para pesquisa: ");
                                try {
                                    int priority = Integer.parseInt(prioritySearch);
                                    searchList = searchTasks(userId, null, null, priority);
                                    for(Task task : searchList){
                                        //JOptionPane.showMessageDialog(null, task.toString());
                                        System.out.println(task.toString());
                                    }
                                } catch (NumberFormatException e) {
                                    JOptionPane.showMessageDialog(frame, "Por favor, insira um número válido para a prioridade.");
                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(frame, "Houve um problema ao buscar as tarefas. Por favor, tente novamente.");
                                }
                                break;
                            case 3:
                                searchList = searchTasks(userId, null, null, null);
                                for(Task task : searchList){
                                    //JOptionPane.showMessageDialog(null, task.toString());
                                    System.out.println(task.toString());
                                }
                                break;
                            default:
                                JOptionPane.showMessageDialog(frame, "Opção inválida !");
                                break;
                        }
                    }catch (Exception e) {
                        JOptionPane.showMessageDialog(frame, e);
                    }
                    break;
                case 2:
                    try {
                        String title = JOptionPane.showInputDialog(frame, "Insira o titulo de sua tarefa: ");
                        if (title == null || title.isEmpty()) {
                            JOptionPane.showMessageDialog(frame, "O título não pode ser vazio.");
                            break;
                        }
                        ObjectId taskIdUpdate = findTaskIdByTitle(userId, title);
                        if (taskIdUpdate != null) {
                            String[] menuUpdate = {"Editar titulo", "Editar descrição","Editar os dois"};
                            int choiceUpdate = JOptionPane.showOptionDialog(frame, "Selecione uma operação:", "Menu de edição", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, menuUpdate, menuUpdate[0]);
                            switch (choiceUpdate) {
                                case 0:
                                    String newTitle = JOptionPane.showInputDialog(frame, "Insira um novo titulo para sua tarefa: ");
                                    if (newTitle == null || newTitle.isEmpty()) {
                                        JOptionPane.showMessageDialog(frame, "O novo título não pode ser vazio.");
                                        break;
                                    }
                                    if (updateTask(taskIdUpdate, userId, newTitle, null)){
                                        JOptionPane.showMessageDialog(frame, "Título atualizado com sucesso!");
                                    }
                                    break;
                                case 1:
                                    String newDescription = JOptionPane.showInputDialog(frame, "Insira uma nova descrição para sua tarefa: ");
                                    if (newDescription == null || newDescription.isEmpty()) {
                                        JOptionPane.showMessageDialog(frame, "A nova descrição não pode ser vazia.");
                                        break;
                                    }
                                    if (updateTask(taskIdUpdate, userId, null, newDescription)){
                                        JOptionPane.showMessageDialog(frame, "Descrição atualizada com sucesso!");
                                    }
                                    break;
                                case 2:
                                    String newTitleOption2 = JOptionPane.showInputDialog(frame, "Insira um novo titulo para sua tarefa: ");
                                    String newDescriptionOption2 = JOptionPane.showInputDialog(frame, "Insira uma nova descrição para sua tarefa: ");
                                    if (newTitleOption2 == null || newTitleOption2.isEmpty() || newDescriptionOption2 == null || newDescriptionOption2.isEmpty()) {
                                        JOptionPane.showMessageDialog(frame, "O novo título e a nova descrição não podem ser vazios.");
                                        break;
                                    }
                                    if (updateTask(taskIdUpdate, userId, newTitleOption2, newDescriptionOption2)){
                                        JOptionPane.showMessageDialog(frame, "Título e descrição atualizados com sucesso!");
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
                case 3:
                    try {
                        String title = JOptionPane.showInputDialog(frame, "Insira o titulo de sua tarefa: ");
                        if (title == null || title.isEmpty()) {
                            JOptionPane.showMessageDialog(frame, "O título não pode ser vazio.");
                            break;
                        }
                        ObjectId taskId = findTaskIdByTitle(userId, title);
                        if (taskId != null) {
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
                case 4:
                    try {
                        String titleCompleted = JOptionPane.showInputDialog(frame, "Insira o titulo de sua tarefa: ");
                        if (titleCompleted == null || titleCompleted.isEmpty()) {
                            JOptionPane.showMessageDialog(frame, "O título não pode ser vazio.");
                            break;
                        }
                        ObjectId taskIdCompleted = findTaskIdByTitle(userId, titleCompleted);
                        if (taskIdCompleted != null) {
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
                case 5:
                    System.out.println("Retornando ao menu de usuário...");
                    return true;
                default:
                    JOptionPane.showMessageDialog(frame, "Escolha uma opção válida do menu.");
                    break;
            }
        }
    }
}
