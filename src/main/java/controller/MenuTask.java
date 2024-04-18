package controller;
import dao.TaskDAO;
import model.Task;
import org.bson.types.ObjectId;
import javax.swing.*;
import java.time.LocalDateTime;
import java.util.List;

public class MenuTask extends TaskDAO {
    public void interfaceMenuTask(ObjectId userId){
        String[] menu = {"Criar tarefa", "Buscar tarefa","Editar tarefa", "Excluir tarefa", "Concluir tarefa", "Sair"};
        List<Task> searchList;

        while (true) {
            int choice = JOptionPane.showOptionDialog(null, "Selecione uma operação:", "Menu de tarefas", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, menu, menu[0]);
            switch (choice) {
                case 0:
                    try {
                        String title = JOptionPane.showInputDialog(null, "Insira o titulo de sua tarefa: ");
                        String description = JOptionPane.showInputDialog(null, "Insira uma descrição da tarefa: ");
                        String status = JOptionPane.showInputDialog(null, "Coloque o seu status personalizado: ");
                        int priority = Integer.parseInt(JOptionPane.showInputDialog(null, "Informe o grau de prioridade entre 0 á 10: "));
                        String tag = JOptionPane.showInputDialog(null, "Insira uma tag para identificar sua tarefa: ");
                        Task newTask = new Task(userId, title, description, status, priority, LocalDateTime.now(),null, tag);
                        if (createTask(newTask)){
                            JOptionPane.showMessageDialog(null, "Tarefa cadastrada com sucesso!");
                        }
                    }catch (Exception e) {
                        System.out.println(e.getMessage());
                        //JOptionPane.showMessageDialog(null, e);
                    }
                    break;
                case 1:
                    try {
                        String[] menuSearch = {"Buscar por título", "Buscar por tag","Buscar por prioridade"};
                        int choiceSearch = JOptionPane.showOptionDialog(null, "Selecione uma operação:", "Menu de pesquisa", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, menuSearch, menuSearch[0]);
                        switch (choiceSearch) {
                            case 0:
                                String titleSearch = JOptionPane.showInputDialog(null, "Insira o titulo para pesquisa: ");
                                searchList = searchTasks(userId, null, titleSearch, null);
                                for(Task task : searchList){
                                    JOptionPane.showMessageDialog(null, task.toString());
                                }
                                break;
                            case 1:
                                String tagSearch = JOptionPane.showInputDialog(null, "Insira a tag para pesquisa: ");
                                searchList = searchTasks(userId, tagSearch, null, null);
                                for(Task task : searchList){
                                    JOptionPane.showMessageDialog(null, task.toString());
                                }
                                break;
                            case 2:
                                String prioritySearch = JOptionPane.showInputDialog(null, "Insira o grau de prioridade para pesquisa: ");
                                searchList = searchTasks(userId, null, null, Integer.parseInt(prioritySearch));
                                for(Task task : searchList){
                                    JOptionPane.showMessageDialog(null, task.toString());
                                }
                                break;
                            default:
                                JOptionPane.showMessageDialog(null, "Opção inválida !");
                                break;
                        }
                    }catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e);
                    }
                    break;
                case 2:
                    try {
                        String title = JOptionPane.showInputDialog(null, "Insira o titulo de sua tarefa: ");
                        ObjectId taskIdUpdate = findTaskIdByTitle(userId, title);
                        if (taskIdUpdate != null) {
                            String[] menuUpdate = {"Editar titulo", "Editar descrição","Editar os dois"};
                            int choiceUpdate = JOptionPane.showOptionDialog(null, "Selecione uma operação:", "Menu de edição", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, menuUpdate, menuUpdate[0]);
                            switch (choiceUpdate) {
                                case 0:
                                    String newTitle = JOptionPane.showInputDialog(null, "Insira um novo titulo para sua tarefa: ");
                                    if (updateTask(taskIdUpdate, userId, newTitle, null)){
                                        JOptionPane.showMessageDialog(null, "Título atualizado com sucesso!");
                                    }
                                    break;
                                case 1:
                                    String newDescription = JOptionPane.showInputDialog(null, "Insira uma nova descrição para sua tarefa: ");
                                    if (updateTask(taskIdUpdate, userId, null, newDescription)){
                                        JOptionPane.showMessageDialog(null, "Descrição atualizada com sucesso!");
                                    }
                                    break;
                                case 2:
                                    String newTitleOption2 = JOptionPane.showInputDialog(null, "Insira um novo titulo para sua tarefa: ");
                                    String newDescriptionOption2 = JOptionPane.showInputDialog(null, "Insira uma nova descrição para sua tarefa: ");
                                    if (updateTask(taskIdUpdate, userId, newTitleOption2, newDescriptionOption2)){
                                        JOptionPane.showMessageDialog(null, "Título e descrição atualizados com sucesso!");
                                    }
                                    break;
                                default:
                                    JOptionPane.showMessageDialog(null, "Opção inválida.");
                                    break;
                            }
                        }

                    }catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e);
                    }
                    break;
                case 3:
                    String title = JOptionPane.showInputDialog(null, "Insira o titulo de sua tarefa: ");
                    ObjectId taskId = findTaskIdByTitle(userId, title);
                    if (taskId != null) {
                        if (deleteTaskById(taskId, userId)) {
                            JOptionPane.showMessageDialog(null, "Tarefa removida com sucesso!");
                        } else {
                            JOptionPane.showMessageDialog(null, "Houve um problema para remover sua tarefa.");
                        }
                    }else {
                        JOptionPane.showMessageDialog(null, "Tarefa não encontrada.");
                    }
                    break;
                case 4:
                    String titleCompleted = JOptionPane.showInputDialog(null, "Insira o titulo de sua tarefa: ");
                    ObjectId taskIdCompleted = findTaskIdByTitle(userId, titleCompleted);
                    if (taskIdCompleted != null) {
                        if (completeTask(taskIdCompleted, userId)) {
                            JOptionPane.showMessageDialog(null, "Tarefa concluida com sucesso!");
                        } else {
                            JOptionPane.showMessageDialog(null, "Houve um problema para concluir sua tarefa.");
                        }
                    }else {
                        JOptionPane.showMessageDialog(null, "Tarefa não encontrada.");
                    }
                    break;
                case 5:
                    System.out.println("Desligando aplicação...");
                    System.exit(0);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Escolha uma opção válida do menu.");
                    break;
            }
        }
    }
}
