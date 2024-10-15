public class TaskTrackerApp {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        // Создаем Epic
        taskManager.createEpic("Пройти спринт 4", "Обучение Java");

        // Получаем Epic по имени для создания Subtask (например, через поиск по коллекции)
        Epic epic = taskManager.findEpicByName("Пройти спринт 4");

        // Создаем Subtask
        if (epic != null) {
            taskManager.createSubtask(epic, "Изучить тему 1", "ООП. Инкапсуляция");

            // Получаем Subtask по имени (например, через поиск по коллекции)
            Subtask subtask = taskManager.findSubtaskByName("Изучить тему 1");

            if (subtask != null) {
                // Создаем Tasks для Subtask
                taskManager.createTask(subtask, "Раздел 1", "Введение в тему");
                taskManager.createTask(subtask, "Раздел 2", "Основы ООП. Принципы и паттерны");
                taskManager.createTask(subtask, "Раздел 3", "Инкапсуляция");
            }
        }

        System.out.println("--------------------------------------------------------------------------");
        System.out.println("Change Task status to IN_PROGRESS");
        System.out.println("--------------------------------------------------------------------------");

        // Получаем Task по имени и обновляем его статус
        Task task = taskManager.findTaskByName("Раздел 1");
        if (task != null) {
            taskManager.updateTaskStatus(task, TaskStatus.IN_PROGRESS);
        }

        System.out.println("--------------------------------------------------------------------------");
        System.out.println("Get all Epics/Subtasks/Task");
        System.out.println("--------------------------------------------------------------------------");

        taskManager.getAllEpics();

        System.out.println("--------------------------------------------------------------------------");
        System.out.println("Change Epic status to DONE");
        System.out.println("--------------------------------------------------------------------------");

        // Получаем Task по имени и обновляем его статус
        Task task1 = taskManager.findTaskByName("Раздел 1");
        if (task1 != null) {
            taskManager.updateTaskStatus(task1, TaskStatus.DONE);
        }

        Task task2 = taskManager.findTaskByName("Раздел 2");
        if (task2 != null) {
            taskManager.updateTaskStatus(task2, TaskStatus.DONE);
        }

        Task task3 = taskManager.findTaskByName("Раздел 3");
        if (task3 != null) {
            taskManager.updateTaskStatus(task3, TaskStatus.DONE);
        }

        System.out.println("--------------------------------------------------------------------------");
        System.out.println("Get all Epics/Subtasks/Task");
        System.out.println("--------------------------------------------------------------------------");

        taskManager.getAllEpics();

    }
}
