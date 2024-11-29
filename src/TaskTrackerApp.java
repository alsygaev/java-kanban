
public class TaskTrackerApp {
    public static void main(String[] args) {

        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        // Создаем две задачи
        Task task1 = new Task("Task 1", "Description Task 1");
        Task task2 = new Task("Task 2", "Description Task 2");
        int task1Id = taskManager.createTask(task1);
        int task2Id = taskManager.createTask(task2);

        // Создаем эпик с тремя подзадачами
        Epic epicWithSubtasks = new Epic("Epic with Subtasks", "Description Epic with Subtasks");
        int epicWithSubtasksId = taskManager.createEpic(epicWithSubtasks);
        Subtask subtask1 = new Subtask("Subtask 1", "Description Subtask 1", epicWithSubtasksId);
        Subtask subtask2 = new Subtask("Subtask 2", "Description Subtask 2", epicWithSubtasksId);
        Subtask subtask3 = new Subtask("Subtask 3", "Description Subtask 3", epicWithSubtasksId);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);

        // Создаем эпик без подзадач
        Epic epicWithoutSubtasks = new Epic("Epic without Subtasks", "Description of empty epic");
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
