import Managers.InMemoryTaskManager;
import Managers.TaskManager;
import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;

import java.io.IOException;

public class TaskTrackerApp {
    public static void main(String[] args) throws IOException {

        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        // Создаем две задачи
        Task task1 = new Task("Tasks.Task 1", "Description Tasks.Task 1");
        Task task2 = new Task("Tasks.Task 2", "Description Tasks.Task 2");
        int task1Id = taskManager.createTask(task1);
        int task2Id = taskManager.createTask(task2);

        // Создаем эпик с тремя подзадачами
        Epic epicWithSubtasks = new Epic("Tasks.Epic with Subtasks", "Description Tasks.Epic with Subtasks");
        int epicWithSubtasksId = taskManager.createEpic(epicWithSubtasks);
        Subtask subtask1 = new Subtask("Tasks.Subtask 1", "Description Tasks.Subtask 1", epicWithSubtasksId);
        Subtask subtask2 = new Subtask("Tasks.Subtask 2", "Description Tasks.Subtask 2", epicWithSubtasksId);
        Subtask subtask3 = new Subtask("Tasks.Subtask 3", "Description Tasks.Subtask 3", epicWithSubtasksId);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);

        // Создаем эпик без подзадач
        Epic epicWithoutSubtasks = new Epic("Tasks.Epic without Subtasks", "Description of empty epic");
        int epicWithoutSubtasksId = taskManager.createEpic(epicWithoutSubtasks);

        // Запрашиваем созданные задачи в разном порядке и выводим историю
        taskManager.getTaskById(task1Id);
        taskManager.getEpicById(epicWithSubtasksId);
        taskManager.getTaskById(task2Id);
        taskManager.getSubtaskById(subtask1.getId());
        taskManager.getEpicById(epicWithoutSubtasksId);
        taskManager.getSubtaskById(subtask2.getId());
        taskManager.getTaskById(task1Id);
        printHistory(taskManager);

        // Удаляем задачу, которая есть в истории, и проверяем историю
        taskManager.deleteTaskById(task1Id);
        printHistory(taskManager);

        // Удаляем эпик с тремя подзадачами и проверяем, что они исчезли из истории
        taskManager.deleteEpicById(epicWithSubtasksId);
        printHistory(taskManager);
    }

    private static void printHistory(TaskManager manager) {
        System.out.println("\nTasks history:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}
