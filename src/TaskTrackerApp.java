public class TaskTrackerApp {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        // Создаем Epic
        taskManager.createEpic("Пройти спринт 4", "Обучение Java");

        // Получаем Epic по HashCode для создания Subtask
        Epic epic = taskManager.findEpicByHashCode(1653947370);

        // Создаем Subtask
        if (epic != null) {
            taskManager.createSubtask(epic, "Изучить тему 1", "ООП. Инкапсуляция");

            // Получаем Subtask по HashCode
            Subtask subtask = taskManager.findSubtaskByHashCode(1232465449);

            if (subtask != null) {
                // Создаем Tasks для Subtask
                taskManager.createTask(subtask, "Раздел 1", "Введение в тему");
                taskManager.createTask(subtask, "Раздел 2", "Основы ООП. Принципы и паттерны");
                taskManager.createTask(subtask, "Раздел 3", "Инкапсуляция");
            }
        }

        System.out.println("--------------------------------------------------------------------------");
        System.out.println("Get all Epics/Subtasks/Task");
        System.out.println("--------------------------------------------------------------------------");

        taskManager.getAllEpics();

        System.out.println("--------------------------------------------------------------------------");

        System.out.println("\n");

        System.out.println("--------------------------------------------------------------------------");
        System.out.println("Change Task status to IN_PROGRESS");
        System.out.println("--------------------------------------------------------------------------");

        // Получаем Task по HashCode и обновляем его статус
        Task task = taskManager.findTaskByHashCode(571407169);
        if (task != null) {
            taskManager.updateTaskStatus(task, TaskStatus.IN_PROGRESS);
            System.out.println("TaskID: " + task.hashCode() + " with name: " + task.getName()
                    + " was updated. New status: " + task.getStatus() + ".");
        }
        System.out.println("--------------------------------------------------------------------------");

        System.out.println("\n");

        System.out.println("--------------------------------------------------------------------------");
        System.out.println("Get all Epics/Subtasks/Task");
        System.out.println("--------------------------------------------------------------------------");

        taskManager.getAllEpics();

        System.out.println("--------------------------------------------------------------------------");

        System.out.println("\n");

        System.out.println("--------------------------------------------------------------------------");
        System.out.println("Change Epic status to DONE");
        System.out.println("--------------------------------------------------------------------------");

        // Получаем все Taskи по HashCode и обновляем их статус на DONE
        Task task1 = taskManager.findTaskByHashCode(571407169);
        if (task1 != null) {
            taskManager.updateTaskStatus(task1, TaskStatus.DONE);
        }

        Task task2 = taskManager.findTaskByHashCode(-197360900);
        if (task2 != null) {
            taskManager.updateTaskStatus(task2, TaskStatus.DONE);
        }

        Task task3 = taskManager.findTaskByHashCode(-2109248802);
        if (task3 != null) {
            taskManager.updateTaskStatus(task3, TaskStatus.DONE);
        }
        System.out.println("--------------------------------------------------------------------------");

        System.out.println("\n");

        System.out.println("--------------------------------------------------------------------------");
        System.out.println("Get all Epics/Subtasks/Task");
        System.out.println("--------------------------------------------------------------------------");

        taskManager.getAllEpics();

        System.out.println("--------------------------------------------------------------------------");

        System.out.println("\n");

        System.out.println("--------------------------------------------------------------------------");
        System.out.println("Delete Task");
        System.out.println("--------------------------------------------------------------------------");

        // Получаем Task по HashCode для удаления
        Task taskForRemove = taskManager.findTaskByHashCode(571407169);

        taskManager.deleteTask(taskForRemove);

        System.out.println("Task " + taskForRemove.getName() + " was deleted");
        System.out.println("--------------------------------------------------------------------------");

        System.out.println("\n");

        System.out.println("--------------------------------------------------------------------------");
        System.out.println("Get all Epics/Subtasks/Task");
        System.out.println("--------------------------------------------------------------------------");

        taskManager.getAllEpics();

        System.out.println("--------------------------------------------------------------------------");

        System.out.println("\n");

        System.out.println("--------------------------------------------------------------------------");
        System.out.println("Delete Epic");
        System.out.println("--------------------------------------------------------------------------");

        // Получаем Epic по HashCode для удаления
        Epic epicForRemove = taskManager.findEpicByHashCode(1653947370);

        taskManager.deleteEpic(epicForRemove);

        System.out.println("Epic " + epicForRemove.getName() + " was deleted");

        System.out.println("--------------------------------------------------------------------------");

        System.out.println("\n");

        System.out.println("--------------------------------------------------------------------------");
        System.out.println("Get all Epics/Subtasks/Task");
        System.out.println("--------------------------------------------------------------------------");

        taskManager.getAllEpics();

        System.out.println("--------------------------------------------------------------------------");

        System.out.println("\n");

    }
}
